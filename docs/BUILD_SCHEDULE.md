# BUILD SCHEDULE — 30 hours, Red Light / Green Light aware

> Source of truth for the on-site sprint. Print it. Pin it to the desk.
> Every block has a verifiable output. If the block ends and the output
> isn't green, you have to choose: scope-cut or stay-late.

## Block legend

| Symbol | Meaning                                                                     |
| ------ | --------------------------------------------------------------------------- |
| 🟢     | **Green Light** — laptop + phone, both build machines allowed               |
| 🔴     | **Red Light** — phone only via Office Kit (laptop closed as build machine)  |
| ⚪     | **Evaluation** — outside the split, scored checkpoint                       |
| 🎤     | **Pitch** — live, on stage                                                 |
| 🏆     | **Awards**                                                                  |

---

## SATURDAY 12 SEPT 2026

### 🟢 10:00 — Block 0 · Setup (60 min)
**Laptop + phone. Do this while you still can.**

- [ ] `gh repo clone Rakshi2609/dayloop` (or pull latest)
- [ ] Open in Android Studio (Giraffe or newer)
- [ ] Sync Gradle (the bundled llama.cpp AAR pulls ~200MB)
- [ ] `scripts/download_models.sh` — fetches Qwen2.5-1.5B Q4_K_M (~1.1GB) and Whisper-tiny (~75MB) into `models/`
- [ ] Run the stub MainActivity on the iQOO loaner — confirm the app launches
- [ ] Office Kit teach-in at 10:30 — don't skip, this is 10% of your score

**Output:** `BUILD_OK` logged from MainActivity, both models on device storage.

### 🔴 11:00 — Block 1 · LLM on the phone (2h)
**Red Light. Laptop closed. You're coding in Android Studio mirroring to the phone via Office Kit.**

- [ ] Wire llama.cpp JNI into the app
- [ ] Load Qwen2.5-1.5B into the NPU-backed context
- [ ] Send a fixed prompt: `"Summarize: I went to the gym and met Priya"`
- [ ] Receive completion in <2s
- [ ] Log inference latency to a file in app's external dir

**Output:** `LLM_OK` with latency < 2.5s for a 50-token completion.

### 🔴 13:00 — Block 2 · Whisper + voice input (90 min)
**Red Light continues.**

- [ ] Wire whisper.cpp JNI
- [ ] Record 5s from mic, transcribe
- [ ] Pass transcript to LLM: `"Rewrite as a todo: <transcript>"`
- [ ] Display the result on a debug screen

**Output:** Speak → see rewritten todo on screen.

### 🟢 14:30 — Block 3 · Office Kit log streaming (45 min)
**Green Light. Laptops open.**

- [ ] Office Kit clipboard channel: every 5s, copy the latest log line
- [ ] On laptop: `while true; do pbpaste; sleep 5; done` (or the cross-platform equivalent)
- [ ] File channel: write `dayloop.log` to phone's shared dir, laptop tails it

**Output:** Laptop terminal showing real-time DAYLOOP logs.

### 🟢 15:15 — Block 4 · Compose UI scaffold (75 min)
**Green Light. The UI is the demo surface — make it pretty.**

- [ ] Status card (live mode chip)
- [ ] Day-so-far timeline
- [ ] Settings (sensitivity sliders per detector, on/off toggles)
- [ ] Recap preview screen

**Output:** App navigable end-to-end with fake data.

### 🔴 16:30 — Block 5 · Sensor triggers (2.5h)
**Red Light. Phone only.**

- [ ] `DrivingDetector` — accel + gyro variance over 5s window, threshold tuned
- [ ] `MeetingDetector` — face-down (proximity) + still (accel magnitude) + ambient light drop
- [ ] `DoomScrollDetector` — screen-on for >2min + accel random (variance > threshold)
- [ ] `FocusBlock` — FP long-press intent → start 25-min timer
- [ ] `BedtimeDetector` — 11pm + screen-off for >30min + audio silence (mic RMS < threshold)
- [ ] Wire each to a `Trigger` event bus
- [ ] Each trigger logs to `dayloop.log` (HackTracker sees this)

**Output:** All 5 detectors fire correctly in unit tests + manual demo.

### ⚪ 19:00 — EVAL ROUND 1 (3h, scored)
**Outside the split. Mentors + early scoring.**

- [ ] Push latest APK to staging
- [ ] Show the running app to the assigned mentor
- [ ] Get 1 specific piece of feedback (write it down)
- [ ] Eat. Real food, not samosas.

### 🔴 22:00 — Block 6 · Halo + haptics + IR (2h)
**Red Light. The wow-ware.**

- [ ] `HaloController` — try vendor SDK, fall back to RGB ring overlay
  - Blue / Red / Amber / Green / White per detector state
- [ ] `HapticPatterns` — define 5 patterns (1-3-1 for ack, 3-pulse for stress, slow-pulse for focus, sharp for IR fired, gentle for bedtime)
- [ ] `IrBlaster` — `ConsumerIrManager.transmit(38000, [preamble, on, off, on])` to the test smart plug
- [ ] Wire each trigger → color + haptic + (sometimes) IR
- [ ] Document the SDK gap in `docs/HALO_NOTES.md` if it fell back

**Output:** Each trigger visibly + tangibly fires. Demo-able.

### 🟢 00:00 — Block 7 · Wrap or push (6.5h)
**Green Light. The overnight window. Choose: sleep, or push.**

**Default (recommended):** sleep 6h, eat breakfast, return at 06:30 sharp.

**If you're behind:**
- [ ] Cut a detector (drop `BedtimeDetector` first — least visible in demo)
- [ ] Or cut the doom-scroll haptic pattern (smallest user-facing impact)
- [ ] Do NOT cut the 3 core demos: driving, meeting, focus+IR

**If you're ahead:**
- [ ] Add the bedtime recap draft (LLM summarizes day-so-far into 3 bullets)
- [ ] Wire Task Tapper webhook for "log this as a task"

### 🔴 06:30 — Block 8 · Polish + edge cases (2.5h)
**Red Light. The morning push.**

- [ ] App doesn't crash if LLM is still loading (splash + status text)
- [ ] Triggers don't false-fire during Office Kit mirroring
- [ ] Recap draft works end-to-end with one tap
- [ ] Halo color transitions are smooth, not flickery
- [ ] Haptic patterns feel distinct (test blind: which pattern is which?)

**Output:** App survives 5 consecutive demo runs without a crash.

### 🟢 09:00 — Block 9 · Backend + final integration (3h)
**Green Light. Wire it to your existing products.**

- [ ] Task Tapper PATCH /api/tasks endpoint — add a "captured-by-dayloop" tag
- [ ] Danzo /api/bot/tasks/due — inject a "dayloop-recap" task
- [ ] Office Kit clipboard channel: copy recap → user pastes into WhatsApp
- [ ] Settings screen: connect Task Tapper API key (paste from your dashboard)
- [ ] Test the full loop on a clean install

**Output:** App talks to your deployed products.

### ⚪ 12:00 — EVAL ROUND 2 (75 min, scored)
**All teams judged at tables. Demo on the iQOO 15.**

- [ ] Run the 3-min pitch demo live
- [ ] Show the office kit log stream
- [ ] Show the Task Tapper task that was created
- [ ] Don't break. If something breaks, narrate the bug and move on. Don't pretend.

---

## SUNDAY 13 SEPT 2026 (cont.)

### 🎤 13:45 — FINAL PITCH (3 min)
**Live to the full jury. See `docs/PITCH.md`.**

- [ ] Demo runs in this exact order: driving → meeting → focus+IR → recap
- [ ] Halo colors change visibly
- [ ] IR fires visibly (a click from the room's smart plug)
- [ ] Recap appears in WhatsApp

### 🏆 15:30 — AWARDS
**Top 6 advance. 3 students + 3 working pros.**

---

## Scope-cut ladder (in priority order)

If you're falling behind, cut from the BOTTOM. These are the first to go:

1. ❌ BedtimeDetector + recap draft (lowest visibility, can ship as "coming soon")
2. ❌ DoomScrollDetector (smallest user-visible impact)
3. ❌ GymDetector (was never in the core 4 anyway)
4. ❌ Geofence (skip if location permissions are a time sink)
5. ❌ Task Tapper webhook (ship as "logs locally, exports JSON")

**The last 3 to cut, in order:** IR blaster focus block, 4D haptic patterns, Halo ring colors.
These ARE the iQOO pitch. Keep them.

## What MUST work for the pitch

- [x] 3 sensor detectors: driving, meeting, focus (FP trigger)
- [x] Halo ring color changes per state
- [x] 4D haptic fires once per trigger
- [x] IR blaster fires once during focus block
- [x] LLM responds to a fixed prompt in <3s
- [x] Recap draft appears on screen
- [x] App doesn't crash mid-demo
- [x] Office Kit log stream visible on laptop

That's the demo. Everything else is bonus.
