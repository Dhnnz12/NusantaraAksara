package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusantaraaksara.R
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.components.AksaraDropdown
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransliterasiScreen(
    viewModel: AksaraViewModel,
    settingsViewModel: SettingsViewModel, // Tambahkan parameter ini
    onBack: () -> Unit
) {
    // --- STATE PENGATURAN ---
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    // Kamus Lokal
    val strings = object {
        val title = if (currentLang == "id") "Transliterasi." else "Transliteration."
        val selectTarget = if (currentLang == "id") "Pilih Aksara Target" else "Select Target Script"
        val latinText = if (currentLang == "id") "Teks Latin" else "Latin Text"
        val placeholder = if (currentLang == "id") "Ketik kata di sini..." else "Type words here..."
        val resultLabel = if (currentLang == "id") "Hasil" else "Result"
        val emptyHint = if (currentLang == "id") "Ketuk teks latin untuk konversi" else "Type latin text to convert"
    }

    var inputText by remember { mutableStateOf("") }
    val listAksara by viewModel.aksaraList
    var selectedAksara by remember { mutableStateOf<Aksara?>(null) }
    val hasil by viewModel.hasilTransliterasi

    val Poppins = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_medium, FontWeight.Medium)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) Color(0xFF121212) else Color(0xFFFBFBFB))
    ) {
        // --- 1. HEADER ---
        Surface(
            modifier = Modifier.fillMaxWidth().height(140.dp),
            color = if (isDark) Color(0xFF1A1614) else BrownDusk,
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, end = 24.dp, top = 48.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = strings.title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // --- 2. KONTEN UTAMA ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(160.dp))

            Text(
                text = strings.selectTarget,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                color = if (isDark) GoldenHeritage else BrownDusk,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Note: Pastikan AksaraDropdown internalnya juga mendukung Dark Mode
            AksaraDropdown(
                options = listAksara,
                onSelected = { aksara ->
                    selectedAksara = aksara
                    inputText = ""
                    viewModel.loadAturan(aksara.id)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = strings.latinText,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                color = if (isDark) GoldenHeritage else BrownDusk,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    viewModel.konversiTeks(it, selectedAksara?.id ?: 1)
                },
                placeholder = { Text(strings.placeholder, color = Color.Gray, fontFamily = Poppins) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isDark) GoldenHeritage else BrownDusk,
                    unfocusedBorderColor = if (isDark) Color.DarkGray else Color.LightGray,
                    focusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color.White,
                    unfocusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color.White,
                    focusedTextColor = if (isDark) Color.White else Color.Black,
                    unfocusedTextColor = if (isDark) Color.White else Color.Black,
                    cursorColor = if (isDark) GoldenHeritage else BrownDusk
                ),
                textStyle = TextStyle(fontFamily = Poppins, fontSize = 16.sp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "${strings.resultLabel} (${selectedAksara?.nama ?: (if(currentLang=="id") "Aksara" else "Script")})",
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                color = if (isDark) GoldenHeritage else BrownDusk,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 4.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize().padding(20.dp)
                ) {
                    Text(
                        text = hasil.ifEmpty { strings.emptyHint },
                        fontSize = if (hasil.isEmpty()) 14.sp else 34.sp,
                        color = if (hasil.isEmpty()) Color.Gray else (if (isDark) Color.White else BrownDusk),
                        fontFamily = Poppins,
                        textAlign = TextAlign.Center,
                        lineHeight = 45.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}