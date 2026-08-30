# DAYLOOP Scripts

## download_models.sh

Fetches the on-device AI models on first run. Run once after cloning.

```bash
#!/usr/bin/env bash
# Downloads Qwen2.5-1.5B-Instruct (Q4_K_M) and Whisper-tiny.en (int8)
# for the DAYLOOP Android app.
# Run from repo root.

set -euo pipefail

MODELS_DIR="${MODELS_DIR:-./models}"
mkdir -p "$MODELS_DIR"

# 1. Qwen2.5-1.5B-Instruct (Q4_K_M, ~1.1GB)
QWEN_FILE="qwen2.5-1.5b-instruct-q4_k_m.gguf"
if [ ! -f "$MODELS_DIR/$QWEN_FILE" ]; then
  echo "[1/2] Downloading Qwen2.5-1.5B-Instruct Q4_K_M (~1.1GB)..."
  curl -L --fail \
    "https://huggingface.co/Qwen/Qwen2.5-1.5B-Instruct-GGUF/resolve/main/$QWEN_FILE" \
    -o "$MODELS_DIR/$QWEN_FILE"
else
  echo "[1/2] Qwen model already present, skipping."
fi

# 2. Whisper-tiny.en (int8, ~75MB)
WHISPER_FILE="ggml-tiny.en.bin"
if [ ! -f "$MODELS_DIR/$WHISPER_FILE" ]; then
  echo "[2/2] Downloading Whisper-tiny.en (int8, ~75MB)..."
  curl -L --fail \
    "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/$WHISPER_FILE" \
    -o "$MODELS_DIR/$WHISPER_FILE"
else
  echo "[2/2] Whisper model already present, skipping."
fi

echo "Done. Models in $MODELS_DIR"
ls -lh "$MODELS_DIR"
```

## hacktracker_replay.sh

Generates a fake sensor stream for testing triggers without holding the phone.
Useful for the laptop half of Green Light build.

```bash
#!/usr/bin/env bash
# Streams fake sensor events to stdout. Pipe into adb logcat to test triggers.

INTERVAL="${INTERVAL:-0.1}"  # seconds between events
DURATION="${DURATION:-30}"   # total run time

echo "Replaying fake sensor stream for ${DURATION}s at ${INTERVAL}s interval"
end=$((SECONDS + DURATION))

while [ $SECONDS -lt $end ]; do
  # Random walk on accel + gyro
  AX=$(awk 'BEGIN{srand(); print (rand()*4)-2}')
  AY=$(awk 'BEGIN{srand(); print (rand()*4)-2}')
  AZ=$(awk 'BEGIN{srand(); print (rand()*4)-2+9.8}')  # gravity bias
  GX=$(awk 'BEGIN{srand(); print (rand()*0.5)-0.25}')
  GY=$(awk 'BEGIN{srand(); print (rand()*0.5)-0.25}')
  GZ=$(awk 'BEGIN{srand(); print (rand()*0.5)-0.25}')

  echo "accel x=$AX y=$AY z=$AZ  gyro x=$GX y=$GY z=$GZ"

  sleep "$INTERVAL"
done
```

## office_kit_tail.sh (laptop side)

Run on the laptop to tail DAYLOOP logs during the build. Requires Office Kit's
file channel to be paired (covered in the Saturday teach-in).

```bash
#!/usr/bin/env bash
# Tails the DAYLOOP log file from the iQOO loaner via Office Kit.
# Pairing instructions ship with the loaner device.

LOG_PATH="${LOG_PATH:-./dayloop.log}"
echo "Tailing $LOG_PATH (Ctrl-C to stop)"

# Fallback if Office Kit isn't paired yet: poll the shared dir every 1s
LAST_SIZE=0
while true; do
  if [ -f "$LOG_PATH" ]; then
    CUR_SIZE=$(stat -c%s "$LOG_PATH" 2>/dev/null || stat -f%z "$LOG_PATH")
    if [ "$CUR_SIZE" -gt "$LAST_SIZE" ]; then
      tail -c +$((LAST_SIZE + 1)) "$LOG_PATH"
      LAST_SIZE=$CUR_SIZE
    fi
  fi
  sleep 1
done
```
