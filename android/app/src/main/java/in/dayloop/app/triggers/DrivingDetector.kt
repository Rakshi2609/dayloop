package in.dayloop.app.triggers

import in.dayloop.app.sensors.MotionSample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.transform

/**
 * DrivingDetector — emits a DrivingEvent when sustained motion exceeds a
 * threshold over a 5-second rolling window. Tunable; defaults are
 * calibrated for a hand-held phone in a moving vehicle.
 */
class DrivingDetector(
    private val windowMs: Long = 5_000L,
    private val minFastFraction: Float = 0.6f,
    private val minGyroVariance: Float = 0.10f
) {
    sealed class State { object Idle : State(); object Active : State() }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    fun detect(samples: Flow<MotionSample>): Flow<State> = samples
        .transform { s -> if (s.isFast) emit(s) }
        .let { fastSamples ->
            kotlinx.coroutines.flow.flow {
                val window = ArrayDeque<MotionSample>()
                fastSamples.collect { sample ->
                    window.addLast(sample)
                    val cutoff = sample.timestampNanos - windowMs * 1_000_000L
                    while (window.isNotEmpty() && window.first().timestampNanos < cutoff) {
                        window.removeFirst()
                    }
                    val total = window.size
                    if (total >= 20) {
                        val fast = window.count { it.isFast }
                        val variance = window.map { it.gyroMagnitude }.variance()
                        if (fast.toFloat() / total >= minFastFraction && variance >= minGyroVariance) {
                            _state.value = State.Active
                            emit(State.Active)
                        } else if (fast.toFloat() / total < 0.2f) {
                            _state.value = State.Idle
                            emit(State.Idle)
                        }
                    }
                }
            }
        }
}

private fun List<Float>.variance(): Float {
    if (isEmpty()) return 0f
    val mean = average().toFloat()
    return map { (it - mean) * (it - mean) }.average().toFloat()
}
