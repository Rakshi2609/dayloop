package in.dayloop.app.actions

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.provider.Settings

/**
 * Wraps OriginOS 6 system services for the actions that the trigger
 * layer requests. DND, ringer, and auto-reply all live here.
 */
class SystemActions(context: Context) {
    private val appContext = context.applicationContext

    fun setDoNotDisturb(enable: Boolean) {
        val am = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val mode = if (enable) AudioManager.RINGER_MODE_SILENT else AudioManager.RINGER_MODE_NORMAL
            if (am.ringerMode != mode) am.ringerMode = mode
        }
    }

    fun setAutoReply(message: String) {
        // OriginOS exposes this through Settings; for the demo we just log the intent.
        // The pitch claims "auto-reply sent"; the on-site build will wire the
        // vendor CallBackground API if exposed.
        android.util.Log.i("DAYLOOP", "auto-reply → $message")
    }

    fun setStatus(text: String) {
        // Teams / Slack status: not directly settable from a non-IM app.
        // Demo shows the local DAYLOOP status card mirroring this text.
        android.util.Log.i("DAYLOOP", "status → $text")
    }
}

/**
 * Webhook into Task Tapper / Danzo. The action layer of DAYLOOP is the
 * existing products the team already runs in production — this is the bridge.
 */
class BackendWebhook(private val baseUrl: String, private val apiKey: String) {
    /**
     * POST a recap bullet as a Task Tapper task tagged "dayloop-recap".
     * Real implementation uses OkHttp; this stub returns success.
     */
    fun logRecapBullet(bullet: String, timestampMs: Long): Boolean {
        // TODO: OkHttp POST to $baseUrl/api/tasks
        return true
    }

    /**
     * Push a "captured via voice" todo to Task Tapper.
     */
    fun logVoiceTask(text: String): Boolean {
        // TODO: OkHttp POST to $baseUrl/api/tasks
        return true
    }
}
