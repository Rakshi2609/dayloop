# ARCHITECTURE

## High-level

```
┌─────────────────────────────────────────────────────────────┐
│                    iQOO 15 (OriginOS 6)                      │
│                                                              │
│  ┌─────────────┐    ┌──────────────┐    ┌──────────────┐   │
│  │  Sensors     │───▶│  Triggers    │───▶│   Actions    │   │
│  │  - Accel     │    │  - Driving   │    │  - Halo      │   │
│  │  - Gyro      │    │  - Meeting   │    │  - 4D haptic │   │
│  │  - Light     │    │  - Doom      │    │  - IR blast  │   │
│  │  - Proxim.   │    │  - Focus     │    │  - DND       │   │
│  │  - Mic       │    │  - Bedtime   │    │  - TTS       │   │
│  │  - Screen    │    └──────┬───────┘    │  - Webhook   │   │
│  │  - Location  │           │            └──────┬───────┘   │
│  └─────────────┘           │                   │           │
│                            ▼                   ▼           │
│                     ┌──────────────┐    ┌──────────────┐    │
│                     │  LLM writer  │    │  Log stream  │    │
│                     │  Qwen-1.5B   │    │  + Office    │    │
│                     │  (NPU)       │    │    Kit       │    │
│                     └──────────────┘    └──────┬───────┘    │
│                                                │            │
└────────────────────────────────────────────────┼────────────┘
                                                 │ (file, clipboard)
                                                 ▼
                                          ┌──────────────┐
                                          │   Laptop      │
                                          │   terminal    │
                                          └──────────────┘
```

## Data flow — driving detection (worked example)

1. `SensorManager` emits `Sensor.TYPE_ACCELEROMETER` events at 50Hz
2. `DrivingDetector.consume(accel, gyro)` — rolling 5s window
   - if `gyro.variance > 0.5` AND `accel.magnitude > 12` → emit `DrivingEvent(START)`
3. `TriggerBus.emit(DrivingEvent)` → fans out to:
   - `HaloController.setColor(BLUE, pulse=true)`
   - `HapticPatterns.play(PATTERN_ACK)`
   - `DndController.enable("driving")`
   - `LlmWriter.explain("User started driving")` → returns "Driving, will reply"
   - `TtsController.say(nextEvent)`
   - `CallAutoReply.send("Driving, will text back")`
4. `LogStream.write("12:34:56 TRIGGER driving latency=180ms")` → `dayloop.log`
5. Office Kit file channel → laptop tails the log

## Layers

| Layer | Module | Tech | Why |
| --- | --- | --- | --- |
| Sensors | `sensors/*` | Android `SensorManager` | Standard, all iQOO devices have these |
| Triggers | `triggers/*` | Kotlin coroutines + StateFlow | Compose-friendly, easy to test |
| Actions | `actions/*` | Vendor SDK + system services | HAL is wrapped, easy to swap in fallback |
| LLM | `inference/LlamaBridge.kt` | llama.cpp Android JNI | Qwen-1.5B fits in 1.5GB RAM, 16GB device = comfortable |
| ASR | `inference/WhisperBridge.kt` | whisper.cpp Android JNI | Whisper-tiny is 75MB, runs in <300ms on NPU |
| Halo | `halo/HaloController.kt` | Vendor SDK (or RGB fallback) | If SDK ships, easy. If not, fall back is screen overlay |
| Haptics | `haptics/HapticPatterns.kt` | `VibratorManager` + `VibrationEffect.Composition` | 4D = amplitude+frequency control, Android 13+ API |
| IR | `actions/IrBlaster.kt` | `ConsumerIrManager` | Built into Android, no permission needed for transmit |
| UI | `ui/*` | Jetpack Compose | Modern, fast, easy to demo |
| Log | `LogStream.kt` | File + `OfficeKitClipboardChannel` | HackTracker reads file activity |

## On-device LLM choice — Qwen2.5-1.5B-Instruct (Q4_K_M)

| Model | Size | Why we picked it (or didn't) |
| --- | --- | --- |
| **Qwen2.5-1.5B-Instruct Q4_K_M** | ~1.1GB | ✅ Best instruction following at this size, MIT license, great on Snapdragon NPU |
| Phi-3.5-mini-instruct (3.8B Q4) | ~2.3GB | Good but tight on memory with Whisper + system services |
| Llama-3.2-1B-Instruct Q4 | ~0.8GB | Smaller but worse at structured output (recap format) |
| Llama-3.2-3B-Instruct Q4 | ~2.0GB | Borderline on RAM, slower first token |

**Decision:** Qwen2.5-1.5B. Swap to Phi-3.5 if Qwen has drift issues during testing.

## Build split (Red/Green)

| Stage | Light | Reason |
| --- | --- | --- |
| Repo + Gradle sync + model download | 🟢 Green | Big downloads, set up while you can |
| Compose UI scaffold | 🟢 Green | IDE features are easier on laptop |
| Backend integration (Task Tapper / Danzo) | 🟢 Green | curl, terminal, easy debugging |
| LLM + Whisper on phone | 🔴 Red | Demonstrates on-device is the only path |
| Sensor triggers | 🔴 Red | SensorManager is phone-only |
| Halo + haptics + IR | 🔴 Red | Hardware-only by definition |
| Office Kit wiring | 🟢 Green → 🔴 Red | Easier setup on laptop, then real use on phone |

## Failure modes (in order of likelihood)

1. **Monster Halo SDK not exposed to third-party apps**
   - Fallback: full-screen RGB ring overlay at the camera module's physical location
   - Demo impact: -2pts HackTracker, no jury impact
2. **llama.cpp doesn't load on OriginOS 6** (vendor-modified libc)
   - Fallback: use ONNX Runtime Mobile with Qwen ONNX export
   - Demo impact: ~30min swap, no scoring impact
3. **Whisper-tiny too slow on mic input** (>1s latency)
   - Fallback: pre-bake 20 common phrases, fuzzy-match input
   - Demo impact: none for pitch, less impressive for "any voice"
4. **IR smart plug not at the venue**
   - Fallback: use the IR blaster to control a TV/AC on stage (the venue has one)
   - Demo impact: same wow-factor, different device
5. **Office Kit pairing fails**
   - Fallback: use adb file pull (laptop allowed in Green Light for setup)
   - Demo impact: -5pts HackTracker (no real-time stream)
