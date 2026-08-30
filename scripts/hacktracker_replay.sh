#!/usr/bin/env bash
# Streams fake sensor events to stdout. Pipe into adb logcat to test triggers.

INTERVAL="${INTERVAL:-0.1}"  # seconds between events
DURATION="${DURATION:-30}"   # total run time

echo "Replaying fake sensor stream for ${DURATION}s at ${INTERVAL}s interval"
end=$((SECONDS + DURATION))

while [ $SECONDS -lt $end ]; do
  AX=$(awk 'BEGIN{srand(); print (rand()*4)-2}')
  AY=$(awk 'BEGIN{srand(); print (rand()*4)-2}')
  AZ=$(awk 'BEGIN{srand(); print (rand()*4)-2+9.8}')
  GX=$(awk 'BEGIN{srand(); print (rand()*0.5)-0.25}')
  GY=$(awk 'BEGIN{srand(); print (rand()*0.5)-0.25}')
  GZ=$(awk 'BEGIN{srand(); print (rand()*0.5)-0.25}')

  echo "accel x=$AX y=$AY z=$AZ  gyro x=$GX y=$GY z=$GZ"

  sleep "$INTERVAL"
done
