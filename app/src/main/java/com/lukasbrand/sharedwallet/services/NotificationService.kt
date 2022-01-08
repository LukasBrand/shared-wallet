package com.lukasbrand.sharedwallet.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.data.repository.UsersRepository
import javax.inject.Inject

class NotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var usersRepository: UsersRepository

    override fun onMessageReceived(message: RemoteMessage) {
        createNotificationChannel()
        val builder =
            NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_baseline_attach_money_24)
                .setContentTitle(message.notification?.title)
                .setContentText(message.notification?.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val id = message.messageId.hashCode()

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            println(builder.build())
            notify(id, builder.build())
        }
    }

    override fun onNewToken(p0: String) {
        //TODO: Modify user token if updated on server
        //usersRepository.modifyUser()
    }

    override fun onCreate() {
        createNotificationChannel()
        super.onCreate()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.default_notification_channel_id)
        val descriptionText = "Default Channel for all Expense Notifications."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            getString(R.string.default_notification_channel_id),
            name,
            importance
        ).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}