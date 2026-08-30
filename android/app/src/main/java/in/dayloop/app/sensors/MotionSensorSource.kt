package in.dayloop.app.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Motion sample from accelerometer + gyroscope fusion.
 * Emitted at 50Hz when subscribed.
 */
data class MotionSample(
    val accelMagnitude: Float,   // m/s^2 (gravity already included)
    val accelX: Float, val accelY: Float, val accelZ: Float,
    val gyroMagnitude: Float,    // rad/s
    val timestampNanos: Long
) {
    /** True if the device is moving fast (driving / running). */
    val isFast: Boolean get() = abs(accelMagnitude - 9.81f) > 2.5f

    /** True if the device is being held still on a surface. */
    val isStill: Boolean
        get() = abs(accelMagnitude - 9.81f) < 0.4f && gyroMagnitude < 0.05f
}

/**
 * Wraps Android SensorManager for the iQOO 15's motion sensors.
 * Emits [MotionSample]s as a cold Flow. Always cancel the
 * collection scope to release the sensors.
 */
class MotionSensorSource(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accel: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyro: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    fun samples(): Flow<MotionSample> = callbackFlow {
        // Buffer the most recent accel + gyro readings and emit the fused sample.
        var lastAccel = FloatArray(3)
        var lastGyro = FloatArray(3)
        var lastAccelTs = 0L
        var lastGyroTs = 0L

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        lastAccel = event.values.copyOf()
                        lastAccelTs = event.timestamp
                    }
                    Sensor.TYPE_GYROSCOPE -> {
                        lastGyro = event.values.copyOf()
                        lastGyroTs = event.timestamp
                    }
                }
                if (lastAccelTs > 0 && lastGyroTs > 0) {
                    val a = lastAccel; val g = lastGyro
                    val accelMag = sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2])
                    val gyroMag = sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2])
                    trySend(
                        MotionSample(
                            accelMagnitude = accelMag,
                            accelX = a[0], accelY = a[1], accelZ = a[2],
                            gyroMagnitude = gyroMag,
                            timestampNanos = lastAccelTs
                        )
                    )
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        accel?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_GAME) }
        gyro?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_GAME) }

        awaitClose { sensorManager.unregisterListener(listener) }
    }
}
