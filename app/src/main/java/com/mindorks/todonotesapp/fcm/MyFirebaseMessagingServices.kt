package com.mindorks.todonotesapp.fcm
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mindorks.todonotesapp.R


class MyFirebaseMessagingService : FirebaseMessagingService(){
    val TAG ="MyFirebaseMessaging"
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG,message.from!!)
        Log.d(TAG, message.notification?.body.toString())
        setUpNotification(message.notification?.body)
    }
    private fun setUpNotification(body: String?) {
        val channelId = "ToDo Id"
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("ToDo Notes App")
                .setContentText(body)
                .setSound(ringtone)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Todo-Notes", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0,notificationBuilder.build())
    }
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
}