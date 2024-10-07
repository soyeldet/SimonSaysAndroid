package com.example.simonsays

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File

class Leaderboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        printJsonFile(this, "scores.json")

        val listView: ListView = findViewById(R.id.highscore_list_view)
        val backBtn: Button = findViewById(R.id.leaderboardButton)
        val intent = Intent(this, MainActivity::class.java)

        listView.isEnabled = false
        val scores = loadData(this, "scores.json")
        val scoresList = scores.map { "${it.first}: ${it.second}"}

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, scoresList)
        listView.adapter = adapter

        backBtn.setOnClickListener{
            startActivity(intent)
        }
    }

    private fun printJsonFile(context: Context, fileName: String) {
        try {
            val inputStream = context.openFileInput(fileName)
            val reader = BufferedReader(inputStream.reader())
            val jsonString = reader.use { it.readText() }
            Log.d("HighscoreActivity", "JSON File Content: $jsonString")
        } catch (e: Exception) {
            Log.e("HighscoreActivity", "Error reading JSON file", e)
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

            return scores.sortedByDescending { it.second }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return scores
    }

}
