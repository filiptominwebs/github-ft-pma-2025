package com.example.cviko003

import android.os.Bundle
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cviko003.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        title = "Objednávky kola"

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOrder.setOnClickListener {
            val selectedId = binding.rgBikeType.checkedRadioButtonId
            val bike = findViewById<RadioButton>(selectedId)

            val fork = binding.cbFork.isChecked
            val seat = binding.cbSeat.isChecked
            val handleBar = binding.cbHandleBar.isChecked

            val orderText = "Souhrn objednávky: " + "\nTyp kola: ${bike.text}" +
                    "\nPříslušenství: " +
                    (if (fork) "Vidlice; " else "") +
                    (if (seat) "Sedlo; " else "") +
                    (if (handleBar) "Řidítka; " else "")


            binding.tvOrderSummary.text = orderText
        }

        binding.rbM1O.setOnClickListener {
            binding.ivBike.setImageResource(R.drawable.bike1)
        }
        binding.rbM2O.setOnClickListener {
            binding.ivBike.setImageResource(R.drawable.bike2)
        }
        binding.rbM3O.setOnClickListener {
            binding.ivBike.setImageResource(R.drawable.bike3)
        }
    }
}