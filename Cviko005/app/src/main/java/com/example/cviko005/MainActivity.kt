package com.example.cviko005

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cviko005.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.btnShowSnackbar.setOnClickListener {
              Snackbar.make(binding.root, "Ahoj, ja jsem Snackbar", Snackbar.LENGTH_SHORT).setBackgroundTint(0xFF6200EE.toInt())
                  .setTextColor(0xFFFFFFFF.toInt())
                  .setDuration(8000)
                  .setAction( "Zavrit") {
                      Toast.makeText(this, "Snackbar zavreny", Toast.LENGTH_SHORT).show()

                  }
                  .show()
       }

        binding.btnShowToast.setOnClickListener {
            Toast.makeText(this, "Ahoj, ja jsem Toast", Toast.LENGTH_SHORT).show()
        }

    }
}