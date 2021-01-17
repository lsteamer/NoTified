package com.example.notified

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.notified.model.Notification

class NotificationListenerImpl : NotificationListenerService() {

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        var notification: Notification? = null
        sbn?.let {
            notification = Notification(
                it.packageName,
                it.groupKey,
                it.id,
                it.key,
                it.isAppGroup,
                it.isClearable,
                it.isGroup,
                it.isOngoing
            )
        }
        val intent = Intent("com.example.notified")
        intent.putExtra("Notification Code", notification)
        sendBroadcast(intent)

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        var notification: Notification? = null
        val notificationCode: String = sbn?.packageName ?: ""
        val activeNotifications = this.activeNotifications

        if (activeNotifications != null && activeNotifications.isNotEmpty()) {
            for (i in activeNotifications.indices) {
                if (notificationCode == activeNotifications[i].packageName) {
                    sbn?.let {
                        notification = Notification(
                            it.packageName,
                            it.groupKey,
                            it.id,
                            it.key,
                            it.isAppGroup,
                            it.isClearable,
                            it.isGroup,
                            it.isOngoing
                        )
                    }
                    val intent = Intent("com.example.notified")
                    intent.putExtra("Notification Code", notification)
                    sendBroadcast(intent)
                }
            }
        }

    }
}