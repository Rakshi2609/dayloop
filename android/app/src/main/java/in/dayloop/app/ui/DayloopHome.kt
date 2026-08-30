package in.dayloop.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * DAYLOOP home screen.
 * Three regions:
 *  - Status card (live detector state + Halo color)
 *  - Day-so-far timeline (events fired today)
 *  - Settings / trigger toggles
 */
@Composable
fun DayloopHome() {
    val state by rememberDayloopState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DAYLOOP", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { StatusCard(state) }
            item {
                Text(
                    "Day so far",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(state.events) { event ->
                TimelineRow(event)
            }
            item {
                Text(
                    "Triggers",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(state.triggers) { trigger ->
                TriggerRow(trigger, onToggle = { /* TODO */ })
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun StatusCard(state: DayloopState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(state.haloColor)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(state.statusLine, style = MaterialTheme.typography.titleMedium)
                Text(
                    "Listening · ${state.detectorCount} detectors live",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TimelineRow(event: DayEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            event.time,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(72.dp)
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(event.color)
        )
        Spacer(Modifier.width(12.dp))
        Text(event.label, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun TriggerRow(trigger: Trigger, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(trigger.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                trigger.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = trigger.enabled, onCheckedChange = onToggle)
    }
}

// ---- State ----

data class DayEvent(val time: String, val label: String, val color: Color)
data class Trigger(val name: String, val subtitle: String, val enabled: Boolean)

data class DayloopState(
    val statusLine: String,
    val haloColor: Color,
    val detectorCount: Int,
    val events: List<DayEvent>,
    val triggers: List<Trigger>
)

@Composable
fun rememberDayloopState(): State<DayloopState> {
    return remember {
        mutableStateOf(
            DayloopState(
                statusLine = "Idle — your phone is listening.",
                haloColor = Color(0xFF6B7280), // neutral grey
                detectorCount = 0,
                events = emptyList(),
                triggers = listOf(
                    Trigger("Driving", "Detected via gyro + accel variance", true),
                    Trigger("Meeting", "Face-down + still + ambient drop", true),
                    Trigger("Doom-scroll", "Screen-on + accel random > 2 min", true),
                    Trigger("Focus block", "FP long-press → 25 min timer", true),
                    Trigger("Bedtime recap", "11pm + silence → 3-bullet draft", false)
                )
            )
        )
    }
}
