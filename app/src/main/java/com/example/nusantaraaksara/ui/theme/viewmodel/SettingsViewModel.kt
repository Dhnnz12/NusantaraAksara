package com.example.nusantaraaksara.ui.theme.viewmodel

// SettingsViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nusantaraaksara.data.local.PreferenceManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class SettingsViewModel(private val preferenceManager: PreferenceManager) : ViewModel() {
    val isDarkMode = preferenceManager.isDarkMode.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), false
    )

    val language = preferenceManager.language.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "id"
    )

    fun toggleTheme(isDark: Boolean) = viewModelScope.launch {
        preferenceManager.saveTheme(isDark)
    }

    fun changeLanguage(lang: String) = viewModelScope.launch {
        preferenceManager.saveLanguage(lang)
    }

    fun resetDefaults() = viewModelScope.launch {
        preferenceManager.resetToDefault()
    }
}