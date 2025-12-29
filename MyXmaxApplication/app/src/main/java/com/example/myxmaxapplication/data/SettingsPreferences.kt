package com.example.myxmaxapplication.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings_prefs")

data class Settings(
    val language: String = "en",
    val snowEnabled: Boolean = true
)

class SettingsPreferences(private val context: Context) {
    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val SNOW_ENABLED_KEY = booleanPreferencesKey("snow_enabled")
    }

    val settingsFlow: Flow<Settings> = context.dataStore.data
        .map { prefs ->
            val language = prefs[LANGUAGE_KEY] ?: "en"
            val snowEnabled = prefs[SNOW_ENABLED_KEY] ?: true
            Settings(language, snowEnabled)
        }

    suspend fun updateLanguage(lang: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = lang
        }
    }

    suspend fun updateSnowEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SNOW_ENABLED_KEY] = enabled
        }
    }
}

