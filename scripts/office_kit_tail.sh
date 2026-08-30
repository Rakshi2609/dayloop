#!/usr/bin/env bash
# Tails the DAYLOOP log file from the iQOO loaner via Office Kit.
# Pairing instructions ship with the loaner device.

LOG_PATH="${LOG_PATH:-./dayloop.log}"
echo "Tailing $LOG_PATH (Ctrl-C to stop)"

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
