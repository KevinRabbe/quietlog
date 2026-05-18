package com.kevinrabbe.quietlog.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevinrabbe.quietlog.QuietLogApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShoppingReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val application = context.applicationContext as QuietLogApplication
        val repository = application.container.shoppingRepository

        CoroutineScope(Dispatchers.IO).launch {
            val items = repository.observeItems().first()
            val uncheckedCount = items.count { !it.isChecked }
            val checkedCount = items.count { it.isChecked }

            if (uncheckedCount > 0 && checkedCount > 0) {
                val notificationHelper = NotificationHelper(context)
                notificationHelper.showReminderNotification(
                    id = SHOPPING_NOTIFICATION_ID,
                    title = "Forgot something?",
                    notes = "You still have $uncheckedCount items on your shopping list."
                )
            }
        }
    }

    companion object {
        const val SHOPPING_NOTIFICATION_ID = 999L
    }
}
