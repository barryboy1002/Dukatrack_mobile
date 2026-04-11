package com.example.dukatrack
import android.app.Application
import com.example.dukatrack.data.AppDatabase


class DukaTrackApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this)

}
}