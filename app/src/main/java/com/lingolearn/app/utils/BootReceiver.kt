package com.lingolearn.app.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lingolearn.app.data.local.datastore.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var reminderManager: ReminderManager

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                val isEnabled = userPreferences.notificationsFlow.first()
                if (isEnabled) {
                    reminderManager.scheduleReminder()
                }
            }
        }
    }
}
