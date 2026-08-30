package in.dayloop.app.inference

/**
 * Bridge to the on-device LLM. Real implementation loads Qwen2.5-1.5B-Instruct
 * (Q4_K_M) via llama.cpp Android JNI. This stub lets the rest of the app
 * compile and run during the laptop half of Green Light.
 *
 * On the iQOO 15 (SD8 Elite, 16GB) the first token latency should be <500ms
 * and a 50-token completion should land in <2.5s. We log both.
 */
class LlamaBridge {
    fun load(modelPath: String): Boolean {
        // TODO: link llama-android.aar, call nativeLoad(modelPath)
        return false
    }

    /**
     * Synchronous completion. Returns the model's text output and inference
     * stats. Real implementation will be a suspend function on a background
     * dispatcher to keep the UI thread free.
     */
    fun complete(prompt: String, maxTokens: Int = 128): Completion {
        // TODO: link llama-android.aar, call nativeComplete(prompt, maxTokens)
        return Completion(
            text = "[stub] Recap draft for: $prompt",
            firstTokenLatencyMs = 0,
            totalLatencyMs = 0,
            tokensOut = 0
        )
    }

    fun unload() {
        // TODO: nativeUnload()
    }
}

data class Completion(
    val text: String,
    val firstTokenLatencyMs: Long,
    val totalLatencyMs: Long,
    val tokensOut: Int
)

/**
 * Whisper-tiny bridge for on-device speech-to-text. Same stub pattern.
 */
class WhisperBridge {
    fun load(modelPath: String): Boolean {
        // TODO: link whisper-android.aar, call nativeLoad(modelPath)
        return false
    }

    /**
     * Transcribe a 16kHz mono PCM buffer (16-bit signed). Real implementation
     * chunks the audio into 30s windows and stitches results.
     */
    fun transcribe(pcm16kMono: ShortArray): String {
        // TODO: nativeTranscribe(pcm16kMono)
        return "[stub transcript]"
    }

    fun unload() { /* TODO */ }
}
