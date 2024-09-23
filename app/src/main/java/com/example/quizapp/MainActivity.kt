package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var answerGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var resultTextView: TextView

    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var selectedTopic: String

    private val technologyQuestions = listOf(
        Question("1. What is the primary purpose of a DNS (Domain Name System)?", listOf("Translate IP addresses into domain names", "Store information about domains", "Translate domain names into IP addresses", "Protect websites from cyber-attacks"), 2),
        Question("2. Which company created the Java programming language?", listOf("Microsoft", "Sun Microsystems", "Google", "Oracle"), 1),
        Question("3. In database management, what does ACID stand for?", listOf("Accuracy, Compatibility, Integrity, Durability", "Atomicity, Consistency, Isolation, Durability", "Access, Control, Integrity, Development", "Allocation, Connectivity, Isolation, Deletion"), 1),
        Question("4. Which protocol is used for sending emails?", listOf("FTP", "SMTP", "HTTP", "POP3"), 1),
        Question("5. What does SSD stand for in computing?", listOf("Solid State Drive", "System Secure Disk", "Serial Storage Device", "Standard Secure Data"), 0)
    )

    private val geographyQuestions = listOf(
        Question("1. Which river is the longest in the world?", listOf("Amazon", "Nile", "Yangtze", "Mississippi"), 1),
        Question("2. What is the largest island in the world?", listOf("Australia", "Greenland", "Borneo", "Madagascar"), 1),
        Question("3. Which line of latitude passes through the center of Earth?", listOf("Tropic of Cancer", "Tropic of Capricorn", "Equator", "Arctic Circle"), 2),
        Question("4. Which country has the most time zones?", listOf("Russia", "USA", "Canada", "France"), 3),
        Question("5. Which ocean is the largest by surface area?", listOf("Atlantic Ocean", "Indian Ocean", "Southern Ocean", "Pacific Ocean"), 3)
    )

    private val marvelQuestions = listOf(
        Question("1. Who is the first Avenger in the Marvel Cinematic Universe?", listOf("Iron Man", "Thor", "Captain America", "Hulk"), 2),
        Question("2. What is the name of the Asgardian goddess of death?", listOf("Sif", "Frigga", "Hela", "Brunnhilde"), 2),
        Question("3. Who is the main antagonist in Captain America: Civil War?", listOf("Baron Zemo", "Red Skull", "Ultron", "Loki"), 0),
        Question("4. In Avengers: Endgame, which stone does Thanos retrieve from the past?", listOf("Space Stone", "Mind Stone", "Time Stone", "Soul Stone"), 3),
        Question("5. Which character says the phrase, \"I can do this all day\"?", listOf("Tony Stark", "Steve Rogers", "Natasha Romanoff", "Thor"), 1)
    )

    private val mathQuestions = listOf(
        Question("1. What is the value of 2x + 5 = 15?", listOf("2", "5", "7", "10"), 1),
        Question("2. What is the area of a triangle with a base of 10 units and a height of 5 units?", listOf("25 square units", "30 square units", "50 square units", "15 square units"), 0),
        Question("3. What is the value of 7^2 - 3^2?", listOf("40", "46", "44", "48"), 0),
        Question("4. If the ratio of the length to the width of a rectangle is 3:2 and the width is 4 units, what is the length?", listOf("6 units", "8 units", "10 units", "12 units"), 0),
        Question("5. What is the solution to the equation 4(x - 3) = 20?", listOf("5", "8", "10", "3"), 1)
    )

    private var questions: List<Question> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        questionTextView = findViewById(R.id.questionTextView)
        answerGroup = findViewById(R.id.answerGroup)
        submitButton = findViewById(R.id.submitButton)
        resultTextView = findViewById(R.id.resultTextView)

        selectedTopic = intent.getStringExtra("TOPIC") ?: "Maths"

        loadQuestionsForTopic(selectedTopic)

        // Load the first question
        loadQuestion()

        submitButton.setOnClickListener {
            checkAnswer()
        }
    }

    private fun loadQuestionsForTopic(topic: String) {
        questions = when (topic) {
            "Technology" -> technologyQuestions
            "Geography" -> geographyQuestions
            "Marvel" -> marvelQuestions
            "Maths" -> mathQuestions
            else -> technologyQuestions
        }
    }

    private fun loadQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            questionTextView.text = question.text

            val radioButtons = listOf(
                findViewById<RadioButton>(R.id.option1),
                findViewById<RadioButton>(R.id.option2),
                findViewById<RadioButton>(R.id.option3),
                findViewById<RadioButton>(R.id.option4)
            )

            question.options.forEachIndexed { index, option ->
                radioButtons[index].text = option
            }

            answerGroup.clearCheck()
            resultTextView.text = ""
        }
    }

    private fun checkAnswer() {
        val selectedOptionId = answerGroup.checkedRadioButtonId

        if (selectedOptionId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedOptionIndex = when (selectedOptionId) {
            R.id.option1 -> 0
            R.id.option2 -> 1
            R.id.option3 -> 2
            R.id.option4 -> 3
            else -> -1
        }

        // Check if the selected answer is correct
        val correctOptionIndex = questions[currentQuestionIndex].correctAnswerIndex

        if (selectedOptionIndex == correctOptionIndex) {
            resultTextView.text = "Correct!"
            score++
        } else {
            resultTextView.text = "Incorrect!"
        }

        currentQuestionIndex++

        if (currentQuestionIndex < questions.size) {
            loadQuestion()
        } else {
            showFinalScoreDialog()
        }
    }

    private fun showFinalScoreDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_score, null)
        val scoreTextViewDialog: TextView = dialogView.findViewById(R.id.scoreTextView)
        val retryButtonDialog: Button = dialogView.findViewById(R.id.retryButton)
        val backButtonDialog: Button = dialogView.findViewById(R.id.backButton)

        scoreTextViewDialog.text = "Final Score: $score/${questions.size}"

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.show()

        // Retry button listener
        retryButtonDialog.setOnClickListener {
            currentQuestionIndex = 0
            score = 0
            loadQuestionsForTopic(selectedTopic)
            loadQuestion()
            dialog.dismiss()
        }

        // Back to home button listener
        backButtonDialog.setOnClickListener {
            val intent = Intent(this, TopicSelectionActivity::class.java)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
    }
}

data class Question(val text: String, val options: List<String>, val correctAnswerIndex: Int)
