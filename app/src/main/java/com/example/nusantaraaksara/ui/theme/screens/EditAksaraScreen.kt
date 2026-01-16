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
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.EarthySand
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAksaraScreen(
    aksara: Aksara,
    viewModel: AksaraViewModel,
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    val strings = object {
        val title = if (currentLang == "id") "Edit Aksara" else "Edit Script"
        val nameLabel = if (currentLang == "id") "Nama Aksara" else "Script Name"
        val originLabel = if (currentLang == "id") "Asal Daerah" else "Region of Origin"
        val imageLabel = if (currentLang == "id") "URL Gambar" else "Image URL"
        val descLabel = if (currentLang == "id") "Deskripsi" else "Description"
        val saveButton = if (currentLang == "id") "SIMPAN" else "SAVE"
    }

    var nama by remember { mutableStateOf(aksara.nama) }
    var asal by remember { mutableStateOf(aksara.asal_daerah) }
    var deskripsi by remember { mutableStateOf(aksara.deskripsi) }
    var urlGambar by remember { mutableStateOf(aksara.gambar_url) }

    Scaffold(
        topBar = {
            // Header dirapatkan menjadi 100.dp
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)),
                color = if (isDark) Color(0xFF1A1614) else BrownDusk
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                    Text(
                        text = strings.title,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontFamily = PoppinsProfile, // Gunakan PoppinsProfile atau font yang ada
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
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp).verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CustomTextFieldEdit(nama, { nama = it }, strings.nameLabel, isDark)
                    CustomTextFieldEdit(asal, { asal = it }, strings.originLabel, isDark)
                    CustomTextFieldEdit(urlGambar, { urlGambar = it }, strings.imageLabel, isDark)

                    Column {
                        Text(strings.descLabel, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isDark) GoldenHeritage else BrownDusk, modifier = Modifier.padding(bottom = 8.dp))
                        OutlinedTextField(
                            value = deskripsi,
                            onValueChange = { deskripsi = it },
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            shape = RoundedCornerShape(16.dp),
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
                onClick = { viewModel.updateAksara(aksara.id, nama, deskripsi, asal, urlGambar) { onBack() } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isDark) GoldenHeritage else BrownDusk, contentColor = if (isDark) BrownDusk else Color.White),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(strings.saveButton, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CustomTextFieldEdit(value: String, onValueChange: (String) -> Unit, label: String, isDark: Boolean) {
    Column {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isDark) GoldenHeritage else BrownDusk, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isDark) GoldenHeritage else BrownDusk,
                unfocusedBorderColor = if (isDark) Color.DarkGray else Color(0xFFEEEEEE)
            )
        )
    }
}