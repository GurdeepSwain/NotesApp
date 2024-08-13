package com.lakehead.notesapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NoteDisplayActivity : AppCompatActivity() {

    private lateinit var noteTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_display)

        noteTextView = findViewById(R.id.noteTextView)

        // Receive the note data
        val note = intent.getStringExtra("note_key") ?: "No Note"
        noteTextView.text = note

        // Request notification permission if needed (for Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // Permission is already granted, show the notification
                showNotification(note)
            }
        } else {
            // For lower versions, just show the notification
            showNotification(note)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val note = intent.getStringExtra("note_key") ?: "No Note"
            showNotification(note)
        } else {
            // Permission was denied, you might want to inform the user
        }
    }

    private fun showNotification(note: String) {
        createNotificationChannel()

        // Create and show the notification
        val notificationBuilder = NotificationCompat.Builder(this, "note_channel")
            .setSmallIcon(R.drawable.ic_notifications) // Ensure this drawable exists
            .setContentTitle("Quick Notes")
            .setContentText(note)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(this).notify(1, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Note Channel"
            val descriptionText = "Channel for note notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("note_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
