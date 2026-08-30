package in.dayloop.app.sensors

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import in.dayloop.app.MainActivity
import in.dayloop.app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import in.dayloop.app.triggers.DrivingDetector

/**
 * Foreground service that keeps the sensor stream alive while the screen
 * is off. Required for the "phone in your pocket at 11pm" use case.
 *
 * NOTE: This is a stub. The full implementation subscribes to
 * MotionSensorSource.samples() and routes events into the trigger bus.
 */
class SensorForegroundService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        startInForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (job?.isActive == true) return START_STICKY
        job = scope.launch {
            val motion = MotionSensorSource(this@SensorForegroundService)
            val detector = DrivingDetector()
            motion.samples().collectLatest { sample ->
                // TODO: route into TriggerBus; this is the wiring point.
                android.util.Log.i(
                    "DAYLOOP",
                    "motion mag=${sample.accelMagnitude} fast=${sample.isFast} still=${sample.isStill}"
                )
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        job?.cancel()
        scope.cancel()
        super.onDestroy()
    }

    private fun startInForeground() {
        val channelId = "dayloop_sensor"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)
            if (nm.getNotificationChannel(channelId) == null) {
                nm.createNotificationChannel(
                    NotificationChannel(channelId, "DAYLOOP", NotificationManager.IMPORTANCE_LOW)
                )
            }
        }
        val pending = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val notif: Notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("DAYLOOP")
            .setContentText("Listening for context…")
            .setContentIntent(pending)
            .setOngoing(true)
            .build()
        startForeground(1, notif)
    }
}


