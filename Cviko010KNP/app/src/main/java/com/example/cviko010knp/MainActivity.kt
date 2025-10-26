package com.example.cviko010knp

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cviko010knp.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val choices = arrayOf("Rock", "Paper", "Scissors")
    private var playerScore = 0
    private var computerScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        setupPlayButton()
        hideImages()
    }

    private fun setupSpinner() {
        // Create adapter for spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            choices
        )
        binding.choiceSpinner.adapter = adapter
    }

    private fun setupPlayButton() {
        binding.playButton.setOnClickListener {
            playGame()
        }
    }

    private fun hideImages() {
        binding.imageView.visibility = View.INVISIBLE
        binding.imageView2.visibility = View.INVISIBLE
        binding.textView.visibility = View.INVISIBLE
    }

    private fun playGame() {
        val playerChoice = binding.choiceSpinner.selectedItem.toString()

        val computerChoice = choices[Random.nextInt(3)]

        binding.imageView.visibility = View.VISIBLE
        binding.imageView2.visibility = View.VISIBLE
        binding.textView.visibility = View.VISIBLE

        updateChoiceImage(binding.imageView, playerChoice)
        updateChoiceImage(binding.imageView2, computerChoice)

        val result = determineWinner(playerChoice, computerChoice)

        displayResult(playerChoice, computerChoice, result)
    }

    private fun updateChoiceImage(imageView: android.widget.ImageView, choice: String) {
        when (choice) {
            "Rock" -> {
                imageView.setImageResource(R.drawable.rock)
            }
            "Paper" -> {
                imageView.setImageResource(R.drawable.paper)
            }
            "Scissors" -> {
                imageView.setImageResource(R.drawable.scissors)
            }
        }
    }

    private fun determineWinner(player: String, computer: String): GameResult {
        return when {
            player == computer -> GameResult.TIE
            (player == "Rock" && computer == "Scissors") ||
                    (player == "Paper" && computer == "Rock") ||
                    (player == "Scissors" && computer == "Paper") -> {
                playerScore++
                GameResult.PLAYER_WIN
            }
            else -> {
                computerScore++
                GameResult.COMPUTER_WIN
            }
        }
    }

    private fun displayResult(playerChoice: String, computerChoice: String, result: GameResult) {
        val resultText = when (result) {
            GameResult.PLAYER_WIN -> "🎉 You Win!"
            GameResult.COMPUTER_WIN -> "💻 Computer Wins!"
            GameResult.TIE -> "🤝 It's a Tie!"
        }

        binding.textView2.text = """ You chose: $playerChoice Computer chose: $computerChoice $resultText Score - You: $playerScore | Computer: $computerScore""".trimIndent()
        Toast.makeText(this, resultText, Toast.LENGTH_SHORT).show()
    }
    private enum class GameResult {
        PLAYER_WIN,
        COMPUTER_WIN,
        TIE
    }
}