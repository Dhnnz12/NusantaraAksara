package com.example.nusantaraaksara.data.local

// PreferenceManager.kt
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class PreferenceManager(private val context: Context) {
    private val THEME_KEY = booleanPreferencesKey("is_dark_mode")
    private val LANG_KEY = stringPreferencesKey("language_code")

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { it[THEME_KEY] ?: false }
    val language: Flow<String> = context.dataStore.data.map { it[LANG_KEY] ?: "id" }

    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { it[THEME_KEY] = isDark }
    }

    suspend fun saveLanguage(lang: String) {
        context.dataStore.edit { it[LANG_KEY] = lang }
    }

    suspend fun resetToDefault() {
        context.dataStore.edit { it.clear() }
    }
}