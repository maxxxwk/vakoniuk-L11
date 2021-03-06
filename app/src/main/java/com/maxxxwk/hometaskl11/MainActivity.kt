package com.maxxxwk.hometaskl11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput

class MainActivity : AppCompatActivity() {

    private val connectionStateReceiver = ConnectionStateReceiver()
    private val userTapReceiver = UserTapReceiver()

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val SIMPLE_NOTIFICATION_ID = 1
        private const val NOTIFICATION_WITH_BUTTON_ID = 2
        private const val NOTIFICATION_WITH_REPLY_ID = 3
        private const val NOTIFICATION_WITH_PROGRESS_BAR_ID = 4
        private const val NOTIFICATION_WITH_BUTTON_ACTION = "NOTIFICATION_WITH_BUTTON_ACTION"
        private const val REPLY_NOTIFICATION_ACTION = "REPLY_NOTIFICATION_ACTION"
        private const val KEY_TEXT_REPLY = "KEY_TEXT_REPLY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        setupOnClickListeners()
    }

    override fun onStart() {
        super.onStart()
        when (intent.action) {
            NOTIFICATION_WITH_BUTTON_ACTION -> {
                val text = "${getString(R.string.action_notification_button_text)} clicked"
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
            REPLY_NOTIFICATION_ACTION -> {
                val text = getReplyNotificationInput().toString()
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        }
        registerReceiver(connectionStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        registerReceiver(userTapReceiver, UserTapReceiver.getIntentFilter())
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectionStateReceiver)
        unregisterReceiver(userTapReceiver)
    }

    private fun setupOnClickListeners() {
        findViewById<Button>(R.id.simpleNotificationCreateButton).setOnClickListener {
            createSimpleNotification()
        }
        findViewById<Button>(R.id.actionNotificationCreateButton).setOnClickListener {
            createNotificationWithButton()
        }
        findViewById<Button>(R.id.replyNotificationCreateButton).setOnClickListener {
            createNotificationWithReply()
        }
        findViewById<Button>(R.id.progressNotificationCreateButton).setOnClickListener {
            createProgressNotification()
        }
        findViewById<Button>(R.id.openActivityButton).setOnClickListener {
            SecondActivity.start("Some text", 123, this)
        }
        findViewById<Button>(R.id.openMapsButton).setOnClickListener {
            openMaps(50.45, 30.52361)
        }
        findViewById<Button>(R.id.receiverButton).setOnClickListener {
            UserTapReceiver.start(this)
        }
    }

    private fun getNotificationManager(): NotificationManager {
        return getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(this, 0,
                intent, 0)
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.action = action
        }
        return PendingIntent.getActivity(this, 0,
                intent, 0)
    }

    private fun getReplyNotificationInput(): CharSequence? {
        return RemoteInput.getResultsFromIntent(intent)?.getCharSequence(KEY_TEXT_REPLY)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Channel name",
                    NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getNotificationManager()
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createSimpleNotification() {
        val pendingIntent = getPendingIntent()
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(getString(R.string.simple_notification_title))
            setContentText(getString(R.string.simple_notification_text))
            setContentIntent(pendingIntent)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }
        getNotificationManager().notify(SIMPLE_NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationWithButton() {
        val pendingIntent = getPendingIntent(NOTIFICATION_WITH_BUTTON_ACTION)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(getString(R.string.action_notification_title))
            setContentText(getString(R.string.action_notification_text))
            priority = NotificationCompat.PRIORITY_DEFAULT
            addAction(R.drawable.ic_launcher_foreground,
                    getString(R.string.action_notification_button_text),
                    pendingIntent)
        }
        getNotificationManager().notify(NOTIFICATION_WITH_BUTTON_ID, builder.build())
    }

    private fun createNotificationWithReply() {
        val pendingIntent = getPendingIntent(REPLY_NOTIFICATION_ACTION)
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(getString(R.string.reply_notification_hint_label))
                .build()
        val action = NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground,
                getString(R.string.reply_notification_button_label),
                pendingIntent)
                .addRemoteInput(remoteInput)
                .build()
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(getString(R.string.reply_notification_title))
            setContentText(getString(R.string.reply_notification_text))
            priority = NotificationCompat.PRIORITY_DEFAULT
            addAction(action)
        }
        getNotificationManager().notify(NOTIFICATION_WITH_REPLY_ID, builder.build())
    }

    private fun createProgressNotification() {
        val maxProgress = 100
        val currentProgress = 30
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle(getString(R.string.progress_notification_title))
            setContentText(getString(R.string.progress_notification_text))
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setProgress(maxProgress, currentProgress, false)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }
        getNotificationManager().notify(NOTIFICATION_WITH_PROGRESS_BAR_ID, builder.build())
    }

    private fun openMaps(latitude: Double, longitude: Double) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse("geo:$latitude,$longitude")
        }
        startActivity(intent)
    }
}