package com.lingolearn.app

import android.app.Application
import com.lingolearn.app.utils.createNotificationChannel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LingoLearnApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
    }
}
