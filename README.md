# DAYLOOP

> Phone-first, on-device AI that runs the boring parts of your day before you ask.
> Built for the iQOO Hackathon 2026 — Chennai City Battle (Sept 12–13, 2026).

![Track](https://img.shields.io/badge/track-Productivity-7c3aed) ![Category](https://img.shields.io/badge/bucket-Student-2563eb) ![iQOO](tps://img.shields.io/badge/iQOO_15-OriginOS_6-fb7185) ![On--device](https://img.shields.io/badge/AI-100%25_on--device-10b981) ![Prize](https://img.shields.io/badge/prize_target-₹1.5L-fbbf24)

---

## The pitch in one breath

DAYLOOP watches the iQOO 15's sensor stream (motion, audio scene, ambient light, screen-on, location, ultrasonic FP) and **runs the boring parts of your day before you ask.** It detects driving, meetings, doom-scrolling, gym, deep work, and bedtime; acts through the Monster Halo ring, 4D haptics, the IR blaster, OriginOS DND, and Task Tapper / Danzo webhooks; and ends each day by drafting a 3-bullet recap via a local LLM.

**One phone. Ten sensors. One AI. Zero cloud. Zero subscription.**

---

## The iQOO 15 hardware this builds on

| Sensor / API                | What DAYLOOP does with it                          |
| --------------------------- | -------------------------------------------------- |
| Monster Halo RGB ring       | Visual status channel (blue=driving, red=meeting, amber=doom-scroll, green=focus, white=recap ready) |
| 4D vibration motor          | Sub-second confirmation / stress pulse / pomodoro tick |
| Ultrasonic in-display FP    | Long-press = "what's next?" gesture trigger |
| IR blaster                  | Offline room control: dim lights / switch off smart plug / set AC for focus block |
| Accelerometer + Gyro        | Driving detection, doom-scroll detection, still-vs-active, gym motion |
| Ambient light sensor        | Phone-in-pocket vs face-down vs on-table disambiguation |
| Microphone (audio scene)    | Gym clank, traffic, meeting-room chatter, sleep-time silence |
| Screen-on / app foreground  | Doom-scroll counter, focus block enforcement |
| Geofence (coarse location)  | "Arrive home → remind to call mom" |
| Snapdragon 8 Elite NPU      | Runs Qwen2.5-1.5B-Instruct (Q4_K_M) on-device via ONNX Runtime + llama.cpp Android |
| OriginOS 6 hooks            | DND, Smart Remote, AI Photo Enhance, Live Wallpaper triggers, call auto-reply |

---

## Repo layout

```
dayloop/
├── android/                # Native Android app (Kotlin + Jetpack Compose)
│   ├── app/                # Main application
│   │   ├── src/main/java/in/dayloop/app/
│   │   │   ├── sensors/    # SensorManager wrappers (motion, light, mic, fp, ir)
│   │   │   ├── inference/  # On-device LLM (llama.cpp JNI) + Whisper-tiny
│   │   │   ├── halo/       # Monster Halo ring control (vendor SDK + RGB-camera fallback)
│   │   │   ├── haptics/    # 4D vibration patterns
│   │   │   ├── triggers/   # DrivingDetector, MeetingDetector, DoomScrollDetector, FocusBlock, Bedtime, GymDetector
│   │   │   ├── actions/    # DND, IR blast, Task Tapper webhook, Danzo webhook, call auto-reply
│   │   │   ├── ui/         # Compose UI (status, history, settings, live "day-so-far")
│   │   │   └── DayloopApplication.kt
│   │   ├── src/main/res/
│   │   └── src/test/
│   └── gradle/
├── models/                 # Bundled .gguf + .tflite files (downloaded on first run, gitignored)
├── scripts/                # HackTracker demo recorder, Office Kit log streamer
├── docs/
│   ├── PITCH.md            # 3-minute pitch script
│   ├── BUILD_SCHEDULE.md   # 30h Red/Green plan
│   ├── PHASE1_SUBMISSION.md# Paste-ready hackathon form
│   ├── SCORING.md          # How this maps to the 100% rubric
│   └── ARCHITECTURE.md
├── .gitignore
├── LICENSE
└── README.md
```

---

## 30-hour build window (Red/Green split)

| Time (Sat) | Light | What                                                       |
| ---------- | ----- | ---------------------------------------------------------- |
| 10:00      | 🟢 Green | Setup: clone Qwen2.5-1.5B Q4_K_M, set up llama.cpp Android, scaffold Compose UI |
| 11:00      | 🔴 Red   | Run llama.cpp on phone. Wire mic → Whisper-tiny. End-to-end "speak → text" |
| 13:00      | 🟢 Green | Office Kit wired. Eval Round 1 (19:00) prep                 |
| 16:30      | 🔴 Red   | Sensor triggers (driving, meeting, doom-scroll) live       |
| 19:00      | ⚪ Eval  | Round 1 submission                                         |
| 22:00      | 🔴 Red   | IR blaster focus block + 4D haptic patterns                 |
| 00:00      | 🟢 Green | Wrap + rest or push                                        |
| 06:30      | 🔴 Red   | Polish + edge cases + pitch slides                         |
| 09:00      | 🟢 Green | Final integration + Task Tapper / Danzo webhook           |
| 12:00      | ⚪ Eval  | Round 2 — full demo on iQOO 15                             |
| 13:45      | 🎤 Pitch | 3-min live pitch. Hardware demo (voice, Halo, haptic, IR)  |
| 15:30      | 🏆 Awards | Top 6 to Grand Finale                                    |

---

## Hackathon rules this design obeys

- [x] **Demo runs on the iQOO 15** — native Android, OriginOS 6
- [x] **On-device AI at the core** — Qwen2.5-1.5B (Q4_K_M) on Snapdragon NPU, no cloud
- [x] **Office Kit is the laptop bridge** — log stream to terminal in real time (10% rubric)
- [x] **Monster Halo + 4D + IR + FP all used** — 15% HackTracker creative phone use, maxed
- [x] **No web-only app** — every feature touches a sensor or actuator
- [x] **Pitch lands in 3 minutes** — script in `docs/PITCH.md`

See `docs/SCORING.md` for the rubric line-by-line.

---

## Status

🚧 Pre-hackathon scaffolding. **No code yet** — building in the 30h window Sept 12–13 on-site in Chennai.

The repo is open-sourced from day 1 so the team can co-edit and judges can read the diff at the on-site.

---

## Team

- **Rakshith Ganjimut** — Team Lead · `@Rakshi2609` · VIT Chennai, B.Tech CSE AI&Robotics
  Android / on-device LLM / sensor glue / pitch

Open to 1 more teammate in the **student bucket** (your account must be registered as student on the iQOO Hackathon site). DM on WhatsApp if interested.

---

## License

MIT
