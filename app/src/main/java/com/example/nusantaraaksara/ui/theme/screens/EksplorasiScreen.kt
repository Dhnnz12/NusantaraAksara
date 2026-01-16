package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.nusantaraaksara.R
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.*
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EksplorasiScreen(
    viewModel: AksaraViewModel,
    settingsViewModel: SettingsViewModel,
    onBack: () -> Boolean
) {
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    val panduanItems by viewModel.panduanList
    val aksaraList by viewModel.aksaraList
    val isLoading by viewModel.isLoading

    var expandedAksara by remember { mutableStateOf(false) }
    var selectedAksara by remember { mutableStateOf<Aksara?>(null) }

    val Poppins = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_regular, FontWeight.Normal)
    )

    val bgColor = if (isDark) Color(0xFF121212) else Color.White
    val headerColor = if (isDark) Color(0xFF1A1614) else BrownDusk

    Scaffold(
        containerColor = bgColor,
        topBar = {
            // HEADER IDENTIK DENGAN TRANSLITERASI (image_123ab0.png)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), // Tinggi dikurangi agar tidak ada ruang kosong di bawah teks
                color = headerColor,
                shape = RoundedCornerShape(bottomStart = 38.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 28.dp, top = 8.dp), // Padding top sangat kecil agar teks naik mentok
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (currentLang == "id") "Eksplorasi." else "Explore.",
                        color = GoldenHeritage,
                        fontSize = 32.sp, // Ukuran besar dan tebal sesuai image_12950d.png
                        fontWeight = FontWeight.Black,
                        fontFamily = Poppins
                    )
                    Text(
                        text = if (currentLang == "id") "Panduan Aksara Nusantara" else "Archipelago Script Guide",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        fontFamily = Poppins
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            // LABEL INPUT (Sama seperti "Pilih Aksara Target" di Transliterasi)
            Text(
                text = if (currentLang == "id") "Pilih Jenis Aksara" else "Select Script Type",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                color = BrownDusk,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // DROPDOWN BOX (Sama seperti image_123ab0.png)
            ExposedDropdownMenuBox(
                expanded = expandedAksara,
                onExpandedChange = { expandedAksara = !expandedAksara }
            ) {
                OutlinedTextField(
                    value = selectedAksara?.nama ?: (if (currentLang == "id") "Pilih Jenis Aksara" else "Select Script Type"),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAksara) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldenHeritage,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    textStyle = TextStyle(fontFamily = Poppins, fontSize = 14.sp)
                )
                ExposedDropdownMenu(
                    expanded = expandedAksara,
                    onDismissRequest = { expandedAksara = false }
                ) {
                    aksaraList.forEach { aksara ->
                        DropdownMenuItem(
                            text = { Text(aksara.nama, fontFamily = Poppins) },
                            onClick = { selectedAksara = aksara; expandedAksara = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // KONTEN PANDUAN
            if (selectedAksara == null) {
                EmptyStateView(Poppins, currentLang, isDark)
            } else if (isLoading) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GoldenHeritage)
                }
            } else {
                panduanItems.forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "${if(currentLang=="id") "Karakter" else "Character"}: ${item.karakterTarget}",
                                fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 18.sp,
                                color = BrownDusk
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFFF5F5F5)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(item.fileAnimasi)
                                        .decoderFactory(GifDecoder.Factory())
                                        .build(),
                                    contentDescription = null,
                                    modifier = Modifier.size(160.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = item.urutanLangkah,
                                fontFamily = Poppins,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateView(font: FontFamily, lang: String, isDark: Boolean) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(modifier = Modifier.size(80.dp), shape = CircleShape, color = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF1F1F1)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Info, null, tint = Color.Gray, modifier = Modifier.size(32.dp))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = if (lang == "id") "Pilih aksara di atas untuk\nmulai eksplorasi panduan."
            else "Select a script above to\nstart exploring guides.",
            textAlign = TextAlign.Center, color = Color.Gray, fontFamily = font, fontSize = 14.sp
        )
    }
}