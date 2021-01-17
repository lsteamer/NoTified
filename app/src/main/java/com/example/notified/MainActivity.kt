package com.example.notified

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.notified.data.NotifiedProvider
import com.example.notified.model.Notification

class MainActivity : AppCompatActivity() {

    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS =
        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

    private var notifyBroadcasterReceiver: NotifyBroadcasterReceiver? = null
    private var enableNotificationListenerAlertDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isNotificationServiceEnabled()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog!!.show()
        }

        notifyBroadcasterReceiver = NotifyBroadcasterReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.notified")
        registerReceiver(notifyBroadcasterReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notifyBroadcasterReceiver)
    }

    private fun interceptedNotification(notificationCode: Notification?) {
        notificationCode?.let {
            val cv = ContentValues()
            cv.put(NotifiedProvider.GROUP_KEY, it.groupKey)
            cv.put(NotifiedProvider.KEY, it.key)
            cv.put(NotifiedProvider.PACKAGE_NAME, it.packageName)
            contentResolver.insert(NotifiedProvider.CONTENT_URI, cv)
        }
    }

    inner class NotifyBroadcasterReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val receivedNotificationCode = intent.extras?.get("Notification Code") as Notification
            interceptedNotification(receivedNotificationCode)
        }
    }


    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":").toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun buildNotificationServiceAlertDialog(): AlertDialog? {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Notification Listener Service")
        alertDialogBuilder.setMessage("Enable the Notification Listener Service?")
        alertDialogBuilder.setPositiveButton("yes"
        ) { _dialog, id ->
            startActivity(
                Intent(
                    ACTION_NOTIFICATION_LISTENER_SETTINGS
                )
            )
        }
        alertDialogBuilder.setNegativeButton("no"
        ) { dialog, id ->

        }
        return alertDialogBuilder.create()
    }
}