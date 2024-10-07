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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random
import android.content.Context
import android.content.Intent
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader

class StartGame : AppCompatActivity() {
        private lateinit var gridView: GridView
        private var level = 1
        private var imageAdapter: ImageAdapter? = null
        private var colorCorrect = mutableListOf<Int>()
        private var currentStep = 0
        private lateinit var startButton: Button
        private var highscore = 0
        private lateinit var backButton: Button

        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_start_game)

            gridView = findViewById(R.id.grid_)
            startButton = findViewById(R.id.start)
            backButton = findViewById(R.id.back)
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
            backButton.isEnabled = true
            backButton.alpha = 1f
            startButton.setOnClickListener {
                startNewLevel()
                startButton.isEnabled = false
                startButton.alpha = 0.5f
                backButton.isEnabled = false
                backButton.alpha = 0.5f
            }
            backButton.setOnClickListener{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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
            val scoreTxt: TextView = findViewById(R.id.score)
            val highscoreTxt: TextView = findViewById(R.id.highscore)
            gridView.isEnabled = true
            gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                if (currentStep < colorCorrect.size) {
                    if (colorCorrect[currentStep] == position) {
                        currentStep++
                        if (currentStep == colorCorrect.size) {
                            level++
                            startNewLevel()
                            scoreTxt.text = "SCORE: " + (level - 1).toString()
                        }
                    } else {
                        Toast.makeText(this, "You failed!", Toast.LENGTH_LONG).show()
                        gridView.isEnabled = false
                        gridView.alpha = 0.5f
                        currentStep = 0
                        colorCorrect.clear()

                        if (level > highscore) {
                            highscore = level - 1
                            highscoreTxt.text = "HIGHSCORE: $highscore"
                            val playerName = intent.getStringExtra("PlayerName").toString()
                            saveData(this, Score(playerName, highscore), "scores.json")
                        }



                        startGame()
                    }
                }
            }
        }

    data class Score(
        val name: String,
        val playerHighscore: Int
    )

    fun saveData(context: Context, newPlayerScore: Score, fileName: String) {
        val scores = loadData(context, fileName).toMutableList()
        var playerFound = false

        for (i in scores.indices) {
            if (scores[i].first == newPlayerScore.name) {
                if (newPlayerScore.playerHighscore > scores[i].second) {
                    scores[i] = Pair(newPlayerScore.name, newPlayerScore.playerHighscore)
                }
                playerFound = true
            }
        }

        if (!playerFound) {
            scores.add(Pair(newPlayerScore.name, newPlayerScore.playerHighscore))
        }

        val jsonArray = JSONArray()
        for (score in scores) {
            val jsonObject = JSONObject().apply {
                put("name", score.first)
                put("highscore", score.second)
            }
            jsonArray.put(jsonObject)
        }

        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output.write(jsonArray.toString().toByteArray())
        }
    }

    private fun loadData(context: Context, fileName: String): List<Pair<String, Int>> {
        val scores = mutableListOf<Pair<String, Int>>()

        try {
            val inputStream = context.openFileInput(fileName)
            val reader = BufferedReader(inputStream.reader())
            val jsonString = reader.use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name = jsonObject.getString("name")
                val highscore = jsonObject.getInt("highscore")
                scores.add(Pair(name, highscore))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return scores
    }

    }
