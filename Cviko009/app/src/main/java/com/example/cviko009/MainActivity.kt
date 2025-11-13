package com.example.cviko009

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cviko009.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getContent = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.GetContent()
        ) { uri ->
            binding.ivImage.setImageURI(uri)
        }

        binding.btUpload.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.btReset.setOnClickListener {
            resetEffects()
        }

        binding.sbScale.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val scale = progress / 100f
                binding.ivImage.scaleX = scale
                binding.ivImage.scaleY = scale
                binding.tvScaleValue.text = String.format("%.1fx", scale)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.sbRotation.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.ivImage.rotation = progress.toFloat()
                binding.tvRotationValue.text = "${progress}°"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.sbAlpha.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.ivImage.alpha = progress / 100f
                binding.tvAlphaValue.text = "${progress}%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun resetEffects() {
        binding.sbScale.progress = 100
        binding.ivImage.scaleX = 1.0f
        binding.ivImage.scaleY = 1.0f
        binding.tvScaleValue.text = "1.0x"

        binding.sbRotation.progress = 0
        binding.ivImage.rotation = 0f
        binding.tvRotationValue.text = "0°"

        binding.sbAlpha.progress = 100
        binding.ivImage.alpha = 1.0f
        binding.tvAlphaValue.text = "100%"
    }
}