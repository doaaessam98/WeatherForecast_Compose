package com.example.weatherforecast.data.repoImp.alarm

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavDeepLinkBuilder
import com.example.weatherforecast.R
import com.example.weatherforecast.app.Utils.Constants
import com.example.weatherforecast.app.screen.MainActivity
import com.example.weatherforecast.app.screen.home.LocationPermissionScreen
import okhttp3.internal.notify
import javax.inject.Inject

class NotificationManager @Inject constructor(private val context: Context) {

    private val notificationManager = NotificationManagerCompat.from(context)
    val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

    fun sendNotification(
        title: String,
        message: String,
        channelId: String,
        notificationId: Int,
        lat:Double,
        lon:Double
    ) {

        Log.e(TAG, "sendNotification: ${lat}", )
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(Constants.KET_HOME_LAT, lat)
            putExtra(Constants.KEY_HOME_LON, lon)
            putExtra("key","123" )
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.alarm_24)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(soundUri)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
             .setContentIntent(pendingIntent)
             .setAutoCancel(true)

        with(notificationManager) {
            if(ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                )!=PackageManager.PERMISSION_GRANTED
            ) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    LocationPermissionScreen(
//                        context = context,
//                        permission = Manifest.permission.POST_NOTIFICATIONS ,
//                        onPermissionGranted = {
//                            notify(notificationId,builder.build())
//                        },
//                        onPermissionDenied = {
//
//                        },
//                        scaffoldState = null
//                    )
                }
                Log.e(TAG, "sendNotification: ", )
                return
            }
            notify(notificationId,builder.build())
        }


    }

    fun createNotificationChannel(
        id: String,
        name: String,
        descriptionText: String,
        importance: Int
    ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(id, name,importance).apply {
                    description = descriptionText
                    setShowBadge(true)
                }
                notificationManager.createNotificationChannel(channel)

            }
        }










}
