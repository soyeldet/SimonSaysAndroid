package com.example.simonsays

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, StartGame::class.java)
        val startButton = findViewById<Button>(R.id.startGameButton)
        val playerName = findViewById<EditText>(R.id.nameEditText)

        updateButtonState(playerName.text.toString(), startButton)

        playerName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonState(s.toString(), startButton)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        startButton.setOnClickListener {
            intent.putExtra("PlayerName", playerName.text.toString())
            startActivity(intent)
        }
    }

    private fun updateButtonState(input: String, button: Button) {
        button.isEnabled = input.isNotBlank()
        button.alpha = if (button.isEnabled) 1.0f else 0.5f
    }
}
