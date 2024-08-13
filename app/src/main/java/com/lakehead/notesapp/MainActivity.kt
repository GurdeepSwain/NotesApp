package com.lakehead.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var noteEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteEditText = findViewById(R.id.noteEditText)
        val saveNoteButton: Button = findViewById(R.id.saveNoteButton)

        saveNoteButton.setOnClickListener {
            val note = noteEditText.text.toString()
            val intent = Intent(this, NoteDisplayActivity::class.java).apply {
                putExtra("note_key", note)
            }
            startActivity(intent)
        }
    }
}
