package com.example.cviko008asharedpref

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cviko008asharedpref.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        val editor = sharedPref.edit()

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val age = binding.etAge.text.toString().trim()
            if (age.isBlank()) {
                Toast.makeText(this, "Please enter your age", Toast.LENGTH_SHORT).show()
            } else {
                val age = age.toInt()
                val isAdult = binding.cbAdult.isChecked
                if ((age < 18 && isAdult) || (age >= 18 && !isAdult)) {
                    Toast.makeText(this, "Age and adult status do not match", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    editor.putString("name", name)
                    editor.putInt("age", age)
                    editor.putBoolean("isAdult", isAdult)
                    editor.apply()
                    Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnLoad.setOnClickListener {
                val name = sharedPref.getString("name", "No name")
                val age = sharedPref.getInt("age", 0)
                val isAdult = sharedPref.getBoolean("isAdult", false)
                val data = binding.tvResult.text.toString()
                binding.tvResult.text = "$data\nName: $name, Age: $age, Adult: $isAdult"
                binding.etName.setText(name)
                if (age == 0) {
                    binding.etAge.setText("")
                } else {
                    binding.etAge.setText(age.toString())
                }
                binding.cbAdult.isChecked = isAdult
                Toast.makeText(this, "Data loaded", Toast.LENGTH_SHORT).show()
            }

        }
    }
}