# SCORING — how DAYLOOP maps to the 100-point rubric

> The full rubric (from iqoo.reskilll.com) is **75% jury + 25% HackTracker**.
> This doc shows where each of our points come from, so we don't
> accidentally leave a row at zero.

---

## JURY: 75 points total

### End product quality — 30 points
**What we deliver:** A working native Android app on the iQOO 15 that runs
on-device, detects 3+ contexts (driving, meeting, focus), acts through
iQOO hardware, and drafts a daily recap.

- 3 live detectors (driving, meeting, focus) = ~10pts
- Recap draft via local LLM = ~8pts
- Polish (no crashes, smooth UI, good state changes) = ~7pts
- Task Tapper / Danzo integration (real users) = ~5pts

**Estimated: 25–28 / 30**

### Novelty and impact — 20 points
**The pitch:** "First productivity tool that treats the iQOO phone as
the brain + sensors + actuators — not as a screen for a web app."

- Proactive, not reactive (no taps needed) = ~6pts
- LLM explains the action in natural language (vs Tasker / MacroDroid) = ~5pts
- iQOO-exclusive hardware usage (Halo, 4D, IR, FP, NPU) = ~5pts
- 100% on-device (privacy + cost + latency story) = ~4pts

**Estimated: 16–18 / 20**

### Technical depth — 15 points
- On-device LLM (Qwen2.5-1.5B) on Snapdragon NPU = 4pts
- On-device ASR (Whisper-tiny) = 2pts
- Multi-sensor fusion (accel + gyro + light + mic + proximity) = 3pts
- LLM-as-writer with deterministic actor layer = 2pts
- Existing Task Tapper / Danzo backend wired = 2pts
- Robust trigger logic (false-fire prevention) = 2pts

**Estimated: 12–13 / 15**

### Demo and presentation — 10 points
- 3-min pitch with 4 hardware demos = 4pts
- Live Office Kit log stream (proves on-device) = 2pts
- Hook ("I did nothing, my phone ran my day") = 2pts
- Closing line names every iQOO sensor used = 2pts

**Estimated: 8–9 / 10**

**Jury subtotal: ~61–68 / 75**

---

## HACKTRACKER: 25 points total (read from device data, not self-reported)

### Creative phone use — 15 points
**Each unique iQOO API/sensor = points:**

| API / sensor                     | Used? | Points (est) |
| -------------------------------- | ----- | ------------ |
| Microphone (Whisper ASR)         | ✅    | 1.5          |
| Camera (OCR / scene detect)      | ⚪ not in MVP, skip | 0 |
| On-device AI / NPU               | ✅ Qwen-1.5B + Whisper | 3.0 |
| OriginOS vendor hooks (DND, Smart Remote) | ✅ | 2.0 |
| Monster Halo ring (or RGB fallback) | ✅ | 3.0 |
| 4D vibration motor               | ✅    | 2.0          |
| Ultrasonic FP gesture            | ✅    | 1.5          |
| IR blaster                       | ✅    | 2.0          |

**Estimated: 15 / 15** (full marks if Halo SDK ships; -2pts if RGB fallback)

### Office Kit usage — 10 points
**Counts and durations only, per the rules.**

- Log file streamed to laptop via file channel: ✅ (~3pts)
- Clipboard channel for last status copy: ✅ (~2pts)
- Screen mirror during demo (legitimate use): ✅ (~2pts)
- Remote control during Red Light build: ✅ (~3pts)

**Estimated: 8–10 / 10**

**HackTracker subtotal: ~23–25 / 25**

---

## Total: 84–93 / 100

To crack **Top 3 in the student bucket** (₹1.5L) we need ~85+ in Chennai.
This plan targets 88. The biggest swing factors:

1. **Halo SDK availability** — full marks if vendor ships; -3pts if fallback
2. **Demo polish** — pitch scoring is the highest variance (could be 7 or 10)
3. **False fires** — if a trigger misfires during the live demo, we lose 5+ pts on the spot. Test until it doesn't.

---

## Why we should NOT chase 100

Diminishing returns. The last 10 points come from:
- Camera OCR (out of MVP scope)
- Geofence (location permissions add 1h of build + 1h of test)
- GymDetector (not in pitch)

These add risk without changing the outcome bracket. **Ship the 88, not the 95.**
