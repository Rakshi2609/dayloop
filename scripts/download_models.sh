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
