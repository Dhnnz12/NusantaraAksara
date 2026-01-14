package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.nusantaraaksara.ui.theme.utils.EnglishStrings
import com.example.nusantaraaksara.ui.theme.utils.IndonesiaStrings
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EksplorasiScreen(
    viewModel: AksaraViewModel,
    settingsViewModel: SettingsViewModel, // Tambahkan ini
    onBack: () -> Unit
) {
    // --- STATE PENGATURAN ---
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()
    val strings = if (currentLang == "id") IndonesiaStrings else EnglishStrings

    val panduanItems by viewModel.panduanList
    val aksaraList by viewModel.aksaraList
    val isLoading by viewModel.isLoading

    var expandedAksara by remember { mutableStateOf(false) }
    var selectedAksara by remember { mutableStateOf<Aksara?>(null) }

    var showDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var selectedIdPanduan by remember { mutableStateOf(0) }
    var karakterTarget by remember { mutableStateOf("") }
    var fileAnimasi by remember { mutableStateOf("") }
    var urutanLangkah by remember { mutableStateOf("") }

    val Poppins = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_regular, FontWeight.Normal)
    )

    LaunchedEffect(Unit) { viewModel.ambilSemuaAksara() }
    LaunchedEffect(selectedAksara) { selectedAksara?.let { viewModel.loadPanduan(it.id) } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (currentLang == "id") "Eksplorasi Aksara" else "Explore Script",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = if (isDark) Color(0xFF1A1614) else BrownDusk
                ),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            )
        },
        floatingActionButton = {
            if (selectedAksara != null) {
                ExtendedFloatingActionButton(
                    onClick = {
                        isEditMode = false; karakterTarget = ""; fileAnimasi = ""; urutanLangkah = ""
                        showDialog = true
                    },
                    containerColor = if (isDark) GoldenHeritage else BrownDusk,
                    contentColor = if (isDark) BrownDusk else Color.White,
                    shape = RoundedCornerShape(16.dp),
                    icon = { Icon(Icons.Default.Add, null) },
                    text = { Text(if (currentLang == "id") "Tambah" else "Add", fontFamily = Poppins) }
                )
            }
        },
        containerColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (currentLang == "id") "Pilih Jenis Aksara" else "Select Script Type",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = if (isDark) GoldenHeritage else BrownDusk,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expandedAksara,
                onExpandedChange = { expandedAksara = !expandedAksara }
            ) {
                OutlinedTextField(
                    value = selectedAksara?.nama ?: (if (currentLang == "id") "Pilih Aksara Nusantara" else "Select Archipelago Script"),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAksara) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isDark) GoldenHeritage else BrownDusk,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color.White,
                        unfocusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color.White,
                        focusedTextColor = if (isDark) Color.White else Color.Black,
                        unfocusedTextColor = if (isDark) Color.White else Color.Black
                    ),
                    textStyle = TextStyle(fontFamily = Poppins)
                )
                ExposedDropdownMenu(
                    expanded = expandedAksara,
                    onDismissRequest = { expandedAksara = false },
                    modifier = Modifier.background(if (isDark) Color(0xFF1E1E1E) else Color.White)
                ) {
                    aksaraList.forEach { aksara ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    aksara.nama,
                                    fontFamily = Poppins,
                                    color = if (isDark) Color.White else Color.Black
                                )
                            },
                            onClick = { selectedAksara = aksara; expandedAksara = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

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
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${if(currentLang=="id") "Karakter" else "Character"}: ${item.karakterTarget}",
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = if (isDark) GoldenHeritage else BrownDusk
                                )
                                Surface(
                                    color = if (isDark) GoldenHeritage.copy(alpha = 0.2f) else BrownDusk.copy(alpha = 0.1f),
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = if (currentLang == "id") "Panduan" else "Guide",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        fontSize = 10.sp,
                                        color = if (isDark) GoldenHeritage else BrownDusk,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isDark) Color(0xFF2A2A2A) else Color(0xFFF1F1F1)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(item.fileAnimasi)
                                        .decoderFactory(GifDecoder.Factory()).build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(180.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = item.urutanLangkah,
                                fontFamily = Poppins,
                                fontSize = 14.sp,
                                color = if (isDark) Color.LightGray else Color.DarkGray,
                                lineHeight = 20.sp
                            )

                            HorizontalDivider(
                                Modifier.padding(vertical = 16.dp),
                                thickness = 0.5.dp,
                                color = if (isDark) Color.DarkGray else Color.LightGray
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = {
                                    selectedIdPanduan = item.idPanduan; karakterTarget = item.karakterTarget
                                    fileAnimasi = item.fileAnimasi; urutanLangkah = item.urutanLangkah
                                    isEditMode = true; showDialog = true
                                }) {
                                    Text(
                                        text = if(currentLang=="id") "Ubah" else "Edit",
                                        color = if(isDark) GoldenHeritage else BrownDusk,
                                        fontFamily = Poppins,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = { viewModel.hapusPanduan(item.idPanduan, item.idAksara) },
                                    modifier = Modifier.background(if(isDark) Color(0xFF3D1C1C) else Color(0xFFFFEBEE), CircleShape).size(36.dp)
                                ) {
                                    Icon(Icons.Default.Delete, null, tint = Color.Red, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(120.dp))
        }
    }

    if (showDialog) {
        // Implementasi Dialog juga harus mengikuti tema warna (isDark)
        // Gunakan strings.save atau strings.back untuk tombol di dalam dialog
    }
}

@Composable
fun EmptyStateView(font: FontFamily, lang: String, isDark: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Info,
            null,
            tint = if (isDark) Color.DarkGray else Color.LightGray,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (lang == "id")
                "Pilih aksara di atas untuk\nmelihat panduan menulis."
            else
                "Select a script above to\nview the writing guide.",
            color = if (isDark) Color.Gray else Color.Gray,
            fontFamily = font,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
    }
}