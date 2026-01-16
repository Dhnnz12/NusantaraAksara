package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusantaraaksara.R
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.EarthySand
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAksaraScreen(
    viewModel: AksaraViewModel,
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    val Poppins = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_extra_bold, FontWeight.ExtraBold),
        Font(R.font.poppins_medium, FontWeight.Medium)
    )

    val strings = object {
        val title = if (currentLang == "id") "Tambah Aksara" else "Add Script"
        val nameLabel = if (currentLang == "id") "Nama Aksara" else "Script Name"
        val originLabel = if (currentLang == "id") "Asal Daerah" else "Region of Origin"
        val imageLabel = if (currentLang == "id") "URL Gambar" else "Image URL"
        val descLabel = if (currentLang == "id") "Deskripsi" else "Description"
        val descPlaceholder = if (currentLang == "id") "Tulis deskripsi singkat..." else "Write a short description..."
        val saveButton = if (currentLang == "id") "SIMPAN" else "SAVE"
    }

    var nama by remember { mutableStateOf("") }
    var asal by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var urlGambar by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            // Header dirapatkan tinggi menjadi 100.dp
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)),
                color = if (isDark) Color(0xFF1A1614) else BrownDusk
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                    Text(
                        text = strings.title,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        },
        containerColor = if (isDark) Color(0xFF121212) else EarthySand
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CustomTextField(nama, { nama = it }, strings.nameLabel, isDark, currentLang)
                    CustomTextField(asal, { asal = it }, strings.originLabel, isDark, currentLang)
                    CustomTextField(urlGambar, { urlGambar = it }, strings.imageLabel, isDark, currentLang)

                    Column {
                        Text(
                            text = strings.descLabel,
                            fontFamily = Poppins, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                            color = if (isDark) GoldenHeritage else BrownDusk,
                            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = deskripsi,
                            onValueChange = { deskripsi = it },
                            placeholder = { Text(strings.descPlaceholder, color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            textStyle = TextStyle(fontFamily = Poppins, color = if (isDark) Color.White else Color.Black),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (isDark) GoldenHeritage else BrownDusk,
                                unfocusedBorderColor = if (isDark) Color.DarkGray else Color(0xFFEEEEEE),
                                focusedContainerColor = if (isDark) Color(0xFF2A2A2A) else Color(0xFFFBFBFB),
                                unfocusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { viewModel.tambahAksara(nama, deskripsi, asal, urlGambar) { onBack() } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isDark) GoldenHeritage else BrownDusk, contentColor = if (isDark) BrownDusk else Color.White),
                shape = RoundedCornerShape(18.dp),
                enabled = nama.isNotEmpty() && asal.isNotEmpty()
            ) {
                Text(text = strings.saveButton, fontFamily = Poppins, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isDark: Boolean,
    currentLang: String
) {
    val placeholderPrefix = if (currentLang == "id") "Masukkan" else "Enter"
    val Poppins = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_extra_bold, FontWeight.ExtraBold),
        Font(R.font.poppins_medium, FontWeight.Medium)
    )
    Column {
        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) GoldenHeritage else BrownDusk,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("$placeholderPrefix $label", color = Color.Gray, fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(fontFamily = Poppins, fontSize = 15.sp, color = if (isDark) Color.White else Color.Black),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isDark) GoldenHeritage else BrownDusk,
                unfocusedBorderColor = if (isDark) Color.DarkGray else Color(0xFFEEEEEE),
                focusedContainerColor = if (isDark) Color(0xFF2A2A2A) else Color(0xFFFBFBFB),
                unfocusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
            )
        )
    }
}