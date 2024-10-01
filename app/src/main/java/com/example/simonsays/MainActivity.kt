package com.example.simonsays

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var gridView: GridView
    private var level = 1
    private var imageAdapter: ImageAdapter? = null
    private var colorCorrect = mutableListOf<Int>()
    private var currentStep = 0
    private lateinit var startButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.grid_)
        startButton = findViewById(R.id.start)
        imageAdapter = ImageAdapter(this)
        gridView.adapter = imageAdapter
        gridView.isEnabled = false
        gridView.alpha = 0.5f

        startGame()

    }

    private fun startGame() {
        level = 1
        startButton.isEnabled = true
        startButton.alpha = 1f
        startButton.setOnClickListener {
        startNewLevel()
        startButton.isEnabled = false
        startButton.alpha = 0.5f
        }
    }

    private fun startNewLevel() {
        gridView.isEnabled = true
        gridView.alpha = 1f
        colorCorrect.add(Random.nextInt(0, 4))
        currentStep = 0
        displayColors(0)
    }

    private fun displayColors(index: Int) {
        gridView.isEnabled = false
        if (index < colorCorrect.size) {
            Handler(mainLooper).postDelayed({
                imageAdapter?.updateImage(colorCorrect[index])
                displayColors(index + 1)
            }, 1200)
        } else {
            enableUserInput()
        }
    }

    private fun enableUserInput() {
        val score: TextView = findViewById(R.id.score)
        gridView.isEnabled = true
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (currentStep < colorCorrect.size) {
                if (colorCorrect[currentStep] == position) {
                    currentStep++
                    if (currentStep == colorCorrect.size) {
                        level++
                        startNewLevel()
                        score.text = "Score: " + (level - 1).toString()
                    }
                } else {
                    Toast.makeText(this, "You failed", Toast.LENGTH_LONG).show()
                    gridView.isEnabled = false
                    gridView.alpha = 0.5f
                    currentStep = 0
                    colorCorrect.clear()
                    startGame()
                }
            }
        }
    }
}
