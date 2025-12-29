package com.example.myxmaxapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myxmaxapplication.data.SettingsPreferences
import com.example.myxmaxapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SettingsPreferences
    private var countdownJob: Job? = null
    private var settingsJob: Job? = null
    private var snowView: SnowView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = SettingsPreferences(applicationContext)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializace sněhového efektu
        val snowContainer = findViewById<android.view.ViewGroup>(R.id.snow_container)
        snowView = SnowView(this)
        snowContainer.addView(snowView, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT)

        val btnSettings = findViewById<android.widget.ImageButton>(R.id.btn_settings)
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        settingsJob = lifecycleScope.launch {
            prefs.settingsFlow.collectLatest { s ->
                // Updatovani textu podle jazyka
                findViewById<android.widget.TextView>(R.id.tv_title).text = Translations.t(s.language, "TITLE_HOME")
                findViewById<android.widget.TextView>(R.id.tv_days_label).text = Translations.t(s.language, "LABEL_DAYS")
                findViewById<android.widget.TextView>(R.id.tv_hours_label).text = Translations.t(s.language, "LABEL_HOURS")
                findViewById<android.widget.TextView>(R.id.tv_minutes_label).text = Translations.t(s.language, "LABEL_MINUTES")

                // Updatovani sněhu
                snowView?.setSnowEnabled(s.snowEnabled)
            }
        }

        // Spuštění odpočtu
        countdownJob = lifecycleScope.launch {
            CountdownManager.ticker().collectLatest { data ->
                // Update UI
                findViewById<android.widget.TextView>(R.id.tv_days).text = data.days.toString()
                findViewById<android.widget.TextView>(R.id.tv_hours).text = data.hours.toString()
                findViewById<android.widget.TextView>(R.id.tv_minutes).text = data.minutes.toString()
            }
        }
    }


}