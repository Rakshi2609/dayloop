package in.dayloop.app

import android.app.Application

class DayloopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Future: init LLM loader, log stream, vendor SDK probe (Monster Halo)
    }
}
