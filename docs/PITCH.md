# PITCH SCRIPT — 3 minutes, demo on the iQOO 15

> Target: 5-person jury, 3-min hard cap, demo on hardware.
> This is the script. Rehearse it on the train to Chennai.

---

## 0:00 — HOOK (15s)

> "I'm going to do nothing for the next three minutes.
> My phone is going to run my day."

*Phone on table, screen up, you stand back. Camera on you and phone.*

---

## 0:15 — SETUP (20s)

> "DAYLOOP. Phone-first AI on the iQOO 15.
> No cloud. No subscription. No taps. The phone already
> knows what I'm doing — driving, meeting, doom-scrolling,
> focusing, sleeping — and acts before I do.
> Watch."

*Pick up phone, show the home screen — the DAYLOOP app icon and one small
"Live" status card showing "Listening". Put it back down.*

---

## 0:35 — DEMO #1: Driving (35s)

> "I'm driving now."

*Shake phone sharply + gyro data spiking on screen mirror (Office Kit
showing it on the laptop, which is OPEN for the demo — judges want to see
it work, the rules are about BUILD time, not demo time).*

> "Halo went blue. Phone went silent. Auto-reply went out.
> My next event just got read to me."

*Phone TTS: "Next: standup with Priya in 14 minutes." Halo ring pulses blue.*

> "No cloud. The model that decided this is Qwen-1.5B,
> running on the Snapdragon NPU. Right here."

---

## 1:10 — DEMO #2: Meeting (30s)

> "I'm in a meeting now."

*Put phone face-down. Wait 2s.*

> "Halo red. Do Not Disturb. My Teams status: In a call.
> OriginOS DND via the vendor API. The phone KNEW —
> accelerometer stillness, light sensor drop, face-down
> for 5 seconds."

*Show the "Status: In meeting" card on the Office Kit mirror.*

---

## 1:40 — DEMO #3: Focus block + IR (30s)

> "Now I need to focus."

*Long-press the ultrasonic fingerprint sensor for 1 second.*

> "FP gesture sent the command. Focus block started.
> The IR blaster just dimmed the room — that's my smart
> plug, offline, no Wi-Fi, no app account.
> Halo green. 4D haptic ticks every 5 minutes. When
> it's done, it pings me. Halo goes white."

*Show the IR command firing (system log on the Office Kit mirror).*

---

## 2:10 — DEMO #4: Recap (30s)

> "End of day. 11pm. I open WhatsApp."

*Pull up the WhatsApp chat with yourself (or Danzo contact). Show
the message that was sent automatically:*

```
📅 Your day, in 3 bullets:
• 2.5h deep work on dayloop (focus blocks, 0 interrupts)
• 1 standup, 1 design review
• 47 min doom-scroll caught + 2 nudges
Reply DONE to log to Task Tapper.
```

> "The 3-bullet recap was drafted by a 1.5-billion parameter
> model on this phone. No internet. No API key. No latency.
> It already knows your day — it just writes it down."

---

## 2:40 — CLOSE (20s)

> "The phone stopped being a screen. It's the brain,
> the sensors, and the actuators — all on one device.
> iQOO 15 is the only phone in this room that can do this.
> We used the Halo, the 4D haptics, the IR, the ultrasonic
> FP, the NPU, the OriginOS hooks. Every sensor. One AI.
> Zero cloud.
>
> DAYLOOP. The phone that runs your day."

*Pick up phone. Halo ring glows steady white. Nod.*

---

## Pitch notes for the team

- **Demo order matters** — start loud (driving), escalate visual wow (Halo colors), end quiet (recap). Don't peak with recap.
- **Office Kit mirror is a feature, not a rule** — show the laptop screen in the demo. It proves on-device, not theatre.
- **The "I did nothing" framing wins hearts** — every sentence should imply the user didn't have to act.
- **Hammer iQOO hardware** — say the names out loud. Halo, 4D, ultrasonic FP, IR, Snapdragon NPU. Judges score what they hear.
- **Don't apologize for limits** — Whisper-tiny isn't perfect, the model is small, the heuristics are simple. Say: "we're shipping 3 detections and 1 always-on recap. The architecture scales."
- **Have a Plan B** — if Halo SDK doesn't ship, the fallback is a screen-edge RGB ring overlay. Rehearse with both.

## What the judges will ask (and the answers)

- **"How is this different from Tasker / MacroDroid?"**
  Those are rule engines. DAYLOOP has an LLM that *explains* the action in
  natural language. Tasker fires a rule; DAYLOOP says *why* it fired.
- **"Why not a cloud LLM?"**
  Latency, cost, privacy, and the rubric — the bonus points for on-device
  are explicit. Plus: a cloud LLM can't infer "driving" from sensors in
  200ms without round-tripping.
- **"What if the model hallucinates?"**
  The 3-bullet recap is a draft, not a send. The user taps "DONE" to log it.
  The LLM is a writer, not an actor. The actor layer is deterministic.
- **"Where's the moat?"**
  Sensor-oracle + LLM writer + iQOO hardware triad. No one else is shipping
  on this stack. The Task Tapper / Danzo backend is real users, not a demo.
- **"Solo?"**
  "Yes, 1/3, currently solo. Open to a teammate in the student bucket
  before 8 September."
