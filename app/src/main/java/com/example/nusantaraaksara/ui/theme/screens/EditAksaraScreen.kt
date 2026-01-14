package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

// --- DEFINISI FONT AGAR TIDAK MERAH ---
private val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAksaraScreen(
    aksara: Aksara,
    viewModel: AksaraViewModel,
    settingsViewModel: SettingsViewModel, // Tambahkan ini
    onBack: () -> Unit
) {
    // --- STATE PENGATURAN ---
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    // Kamus Bahasa Lokal
    val strings = object {
        val title = if (currentLang == "id") "Edit Aksara" else "Edit Script"
        val nameLabel = if (currentLang == "id") "Nama Aksara" else "Script Name"
        val originLabel = if (currentLang == "id") "Asal Daerah" else "Region of Origin"
        val imageLabel = if (currentLang == "id") "URL Gambar" else "Image URL"
        val descLabel = if (currentLang == "id") "Deskripsi" else "Description"
        val saveButton = if (currentLang == "id") "SIMPAN PERUBAHAN" else "SAVE CHANGES"
    }

    var nama by remember { mutableStateOf(aksara.nama) }
    var asal by remember { mutableStateOf(aksara.asal_daerah) }
    var deskripsi by remember { mutableStateOf(aksara.deskripsi) }
    var urlGambar by remember { mutableStateOf(aksara.gambar_url) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = strings.title,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = if (isDark) Color(0xFF1A1614) else BrownDusk
                ),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            )
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
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        label = strings.nameLabel,
                        isDark = isDark
                    )
                    CustomTextField(
                        value = asal,
                        onValueChange = { asal = it },
                        label = strings.originLabel,
                        isDark = isDark
                    )
                    CustomTextField(
                        value = urlGambar,
                        onValueChange = { urlGambar = it },
                        label = strings.imageLabel,
                        isDark = isDark
                    )

                    Column {
                        Text(
                            text = strings.descLabel,
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) GoldenHeritage else BrownDusk,
                            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = deskripsi,
                            onValueChange = { deskripsi = it },
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            textStyle = TextStyle(
                                fontFamily = Poppins,
                                color = if (isDark) Color.White else Color.Black
                            ),
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) GoldenHeritage else BrownDusk,
                    contentColor = if (isDark) BrownDusk else Color.White
                ),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = strings.saveButton,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isDark: Boolean
) {
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
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(
                fontFamily = Poppins,
                fontSize = 15.sp,
                color = if (isDark) Color.White else Color.Black
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isDark) GoldenHeritage else BrownDusk,
                unfocusedBorderColor = if (isDark) Color.DarkGray else Color(0xFFEEEEEE),
                focusedContainerColor = if (isDark) Color(0xFF2A2A2A) else Color(0xFFFBFBFB),
                unfocusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
            )
        )
    }
}