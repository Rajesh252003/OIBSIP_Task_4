package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TopicSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)


        val buttonTechnology = findViewById<Button>(R.id.buttonTechnology)
        val buttonGeography = findViewById<Button>(R.id.buttonGeography)
        val buttonMovies = findViewById<Button>(R.id.buttonMovies)
        val buttonMaths = findViewById<Button>(R.id.buttonMaths)

        buttonTechnology.setOnClickListener {
            startQuizActivity("Technology")
        }

        buttonGeography.setOnClickListener {
            startQuizActivity("Geography")
        }

        buttonMovies.setOnClickListener {
            startQuizActivity("Marvel")
        }

        buttonMaths.setOnClickListener {
            startQuizActivity("Maths")
        }
    }

    private fun startQuizActivity(topic: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("TOPIC", topic)
        startActivity(intent)
    }
}
