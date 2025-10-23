package com.example.cviko010hadejcislo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cviko010hadejcislo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var secretNumber = 0
    private var attempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newNumber()

        binding.btnCheck.setOnClickListener {
            val userInput = binding.etInputNumber.text.toString()
            if (userInput.isNotEmpty()) {
                val guessedNumber = userInput.toInt()
                attempts++

                when {
                    guessedNumber < secretNumber -> {
                        binding.tvResult.text = "Too low! Try again."
                    }
                    guessedNumber > secretNumber -> {
                        binding.tvResult.text = "Too high! Try again."
                    }
                    else -> {
                        binding.tvResult.text = "Congratulations! You've guessed the number $secretNumber in $attempts attempts."
                    }
                }
            } else {
                binding.tvResult.text = "Please enter a number."
            }
        }
    }

    public fun newNumber() {
        secretNumber = (1..12).random()
        attempts = 0
        binding.etInputNumber.text.clear()
    }
}