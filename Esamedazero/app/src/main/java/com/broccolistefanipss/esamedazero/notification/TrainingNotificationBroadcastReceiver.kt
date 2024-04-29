package com.broccolistefanipss.esamedazero.notification

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.broccolistefanipss.esamedazero.R

class TrainingNotificationBroadcastReceiver : BroadcastReceiver() {

    private companion object {
        const val PERMISSION_REQUEST_CODE = 101 // Choose a constant for the request code
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        // Retrieve intent extras
        val title = intent.getStringExtra("title") ?: "Reminder"
        val description = intent.getStringExtra("description") ?: "It's time for your workout!"

        // Construct the notification
        val builder = NotificationCompat.Builder(context, "training_channel_id")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted. Request it.
                requestNotificationPermission(context as Activity)
                return
            } else {
                // Permission already granted, display the notification
                notify(101, builder.build()) // Use a unique notification ID
            }
        }
    }

    // Helper function to handle the permission request
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            // Show an explanation to the user (ex: Dialog) about why you need the permission
            // ... (Implement your explanation UI)
        } else {
            // Directly request the permission
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PERMISSION_REQUEST_CODE
            )
        }
    }
}