package com.example.eduapp

import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eduapp.databinding.ActivityQuizBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private var questions: List<Question> = emptyList()
    private var index = 0
    private var score = 0
    private var selectedAnswerIndex: Int? = null
    private var userId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("USER_ID", 0)

        lifecycleScope.launch {
            val db = AppDatabase.getInstance(applicationContext)
            questions = withContext(Dispatchers.IO) { db.questionDao().getRandomQuestions(10) }

            if (questions.isEmpty()) {
                // Seed default questions at runtime and reload
                val seed = listOf(
                    Question(text = "What is the capital of France?", answerA = "Paris", answerB = "Berlin", answerC = "Rome", answerD = "Madrid", correctIndex = 0),
                    Question(text = "Which planet is known as the Red Planet?", answerA = "Venus", answerB = "Mars", answerC = "Jupiter", answerD = "Saturn", correctIndex = 1),
                    Question(text = "What is 2 + 2?", answerA = "3", answerB = "4", answerC = "5", answerD = "6", correctIndex = 1),
                    Question(text = "Who wrote \"Romeo and Juliet\"?", answerA = "Charles Dickens", answerB = "William Shakespeare", answerC = "Leo Tolstoy", answerD = "Mark Twain", correctIndex = 1),
                    Question(text = "What color do you get by mixing red and white?", answerA = "Pink", answerB = "Green", answerC = "Purple", answerD = "Orange", correctIndex = 0),
                    Question(text = "Which animal is known as the King of the Jungle?", answerA = "Elephant", answerB = "Tiger", answerC = "Lion", answerD = "Giraffe", correctIndex = 2),
                    Question(text = "What is the largest ocean on Earth?", answerA = "Atlantic", answerB = "Indian", answerC = "Arctic", answerD = "Pacific", correctIndex = 3),
                    Question(text = "How many continents are there?", answerA = "5", answerB = "6", answerC = "7", answerD = "8", correctIndex = 2),
                    Question(text = "Which gas do plants mainly absorb?", answerA = "Oxygen", answerB = "Nitrogen", answerC = "Carbon Dioxide", answerD = "Hydrogen", correctIndex = 2),
                    Question(text = "What is the boiling point of water at sea level in Celsius?", answerA = "90", answerB = "100", answerC = "110", answerD = "120", correctIndex = 1)
                )

                withContext(Dispatchers.IO) { db.questionDao().insertQuestions(seed) }

                questions = withContext(Dispatchers.IO) { db.questionDao().getRandomQuestions(10) }
            }

            if (questions.isNotEmpty()) showQuestion()
        }

        binding.rgAnswers.setOnCheckedChangeListener { _, checkedId: Int ->
            selectedAnswerIndex = when (checkedId) {
                binding.rb1.id -> 0
                binding.rb2.id -> 1
                binding.rb3.id -> 2
                binding.rb4.id -> 3
                else -> null
            }
            binding.btnNext.isEnabled = selectedAnswerIndex != null
        }

        binding.btnNext.setOnClickListener {
            val q = questions[index]
            selectedAnswerIndex?.let { selected ->
                if (selected == q.correctIndex) score++
            }
            index++
            if (index < questions.size) {
                selectedAnswerIndex = null
                binding.rgAnswers.clearCheck()
                showQuestion()
            } else {
                lifecycleScope.launch {
                    val result = Result(userId = userId, score = score, total = questions.size)
                    val db = AppDatabase.getInstance(applicationContext)
                    withContext(Dispatchers.IO) { db.resultDao().insertResult(result) }

                    val intent = Intent(this@QuizActivity, ResultActivity::class.java).apply {
                        putExtra("SCORE", score)
                        putExtra("TOTAL", questions.size)
                        putExtra("USER_ID", userId)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun showQuestion() {
        val q = questions[index]
        binding.tvProgress.text = getString(R.string.progress_format, index + 1, questions.size)
        binding.tvQuestionText.text = q.text
        binding.rb1.text = q.answerA
        binding.rb2.text = q.answerB
        binding.rb3.text = q.answerC
        binding.rb4.text = q.answerD
        binding.btnNext.isEnabled = false
    }
}
