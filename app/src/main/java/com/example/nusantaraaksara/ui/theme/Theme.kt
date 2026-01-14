package com.example.nusantaraaksara.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Palet warna Dark Mode (REQ-3.3.2)
private val DarkColorScheme = darkColorScheme(
    primary = GoldenHeritage,
    onPrimary = BrownDusk,
    secondary = EarthySand,
    background = Color(0xFF121212), // Hitam elegan
    surface = Color(0xFF1E1E1E),    // Abu-abu gelap untuk kartu
    onBackground = Color.White,
    onSurface = Color.White
)

// Palet warna Light Mode
private val LightColorScheme = lightColorScheme(
    primary = BrownDusk,
    onPrimary = Color.White,
    secondary = GoldenHeritage,
    background = Color.White,
    surface = Color.White,
    onBackground = BrownDusk,
    onSurface = BrownDusk
)

@Composable
fun NusantaraAksaraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false agar warna brand kita tidak berubah-ubah
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}