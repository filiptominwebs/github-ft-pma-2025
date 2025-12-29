package com.example.myxmaxapplication

import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import com.example.myxmaxapplication.data.SettingsPreferences
import com.example.myxmaxapplication.databinding.ActivitySettingsBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: SettingsPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        prefs = SettingsPreferences(applicationContext)
        setContentView(binding.root)

        val rgLanguage = findViewById<RadioGroup>(R.id.rg_language)
        val rbEn = findViewById<RadioButton>(R.id.rb_en)
        val rbCs = findViewById<RadioButton>(R.id.rb_cs)
        val switchSnow = findViewById<SwitchCompat>(R.id.switch_snow)

        // Inicializace UI podle uložených nastavení
        lifecycleScope.launch {
            val s = prefs.settingsFlow.first()
            if (s.language == "cs") rbCs.isChecked = true else rbEn.isChecked = true
            switchSnow.isChecked = s.snowEnabled
        }

        rgLanguage.setOnCheckedChangeListener { _, checkedId ->
            val lang = if (checkedId == R.id.rb_cs) "cs" else "en"
            lifecycleScope.launch { prefs.updateLanguage(lang) }
        }


        switchSnow.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch { prefs.updateSnowEnabled(isChecked) }
        }
    }
}
