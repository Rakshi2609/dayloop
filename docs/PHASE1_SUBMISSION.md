# PHASE 1 SUBMISSION — paste into the iQOO Hackathon dashboard

> **Team:** Wizards
> **Track:** Productivity
> **Bucket:** Student
> **Members:** 1/3 (open to add by 8 Sept 2026)
> **City Battle:** Chennai, 12–13 Sept 2026
> **Lead:** Rakshith Ganjimut · rakshithganjimut@gmail.com

---

## Problem

People live inside their phones, but the phone still waits to be told what to do.
Every app requires a tap, a search, an open. Cloud assistants wake too late and
leak your day. By 11pm you've forgotten what you actually did.

The average iQOO owner touches their phone 150+ times a day — and 80% of those
touches are "open app, do one thing, close, repeat."

## Solution

**DAYLOOP** — a phone-first, on-device AI that observes the iQOO 15's sensor
stream (motion, audio scene, ambient light, screen-on, location, ultrasonic
fingerprint) and **runs the boring parts of your day before you ask.**

It detects driving, meetings, doom-scrolling, gym, deep work, and bedtime; acts
through the Monster Halo ring colors, 4D haptics, the IR blaster, OriginOS DND,
and Task Tapper / Danzo webhooks; and ends each day by drafting a 3-bullet
recap via a local LLM.

**One phone. Ten sensors. One AI. Zero cloud. Zero subscription.**

## Why iQOO

Only the iQOO 15 ships:
- **Monster Halo RGB ring** — a programmable visual status channel
- **4D vibration motor** — sub-second confirmation and stress pulse
- **Ultrasonic in-display fingerprint** — a gesture trigger beyond unlock
- **IR blaster** — an offline room actuator (no Wi-Fi, no cloud, no subscription)
- **Snapdragon 8 Elite NPU + 16GB RAM** — runs a 1.5B-param LLM on-device, no API costs
- **OriginOS 6 hooks** — DND, Smart Remote, AI Photo Enhance, auto-reply

No other phone in the hackathon can run this build end-to-end as designed.

## MVP in 30 hours

- ✅ Driving detection (gyro+accel) → Halo blue, auto-mute, TTS next event
- ✅ Meeting detection (face-down + still + ambient light drop) → Halo red, DND, Teams/Slack status
- ✅ Doom-scroll detection (screen-on + random accel) → Halo amber, 4D haptic nudge
- ✅ Focus block (FP long-press) → 25-min timer, Halo green pulse, IR dim room
- ✅ Bedtime recap (11pm trigger) → local LLM drafts 3-bullet WhatsApp message → send to self
- ✅ Office Kit log stream → laptop terminal mirror for live build visibility

## Differentiator

First productivity tool that treats the iQOO phone as the **brain, sensors, and
actuators** — not as a screen for a web app. The pitch is "I did nothing; my day
ran itself." Most viral 3-minute clip of the day.

## Why us

Rakshith has shipped two relevant products already:
- **Task Tapper** — SaaS task app, deployed on Vercel, used in production
- **Danzo** — WhatsApp daily-task digest, deployed on Render, serving real users

DAYLOOP's action layer is Task Tapper / Danzo. The local LLM on the phone
*talks to* the products we already run. We are not greenfield.

## Tech stack

- **Mobile:** Native Android (Kotlin + Jetpack Compose) on OriginOS 6
- **On-device LLM:** Qwen2.5-1.5B-Instruct (Q4_K_M) via llama.cpp Android JNI
- **On-device ASR:** Whisper-tiny.en (int8) via whisper.cpp Android JNI
- **Sensors:** Android `SensorManager` + `ConsumerIrManager` + `VibratorManager`
- **Vendor hooks:** Monster Halo SDK (or RGB-camera fallback for non-iQOO dev phones)
- **Backend:** Existing Task Tapper REST API + Danzo bot API
- **Office Kit:** clipboard + file channel for log streaming during Red Light

## Risk mitigation

- **Laptop dev allowed in Green Light** — all AI model loading + APK build can happen on laptop
- **Mock sensors during laptop dev** — Compose preview + fake-sensor feed for testing triggers without a phone
- **Halo SDK unavailability** — fall back to screen-edge RGB ring overlay; document trade-off in pitch
- **Whisper-tiny accuracy on Indian English** — pre-bake common phrases; rely on LLM cleanup layer
