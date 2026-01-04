package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel

// Pastikan warna ini sesuai dengan tema aplikasi Anda
val BrownDusk = Color(0xFF4E342E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EksplorasiScreen(viewModel: AksaraViewModel, onBack: () -> Unit) {
    val panduanItems by viewModel.panduanList
    val aksaraList by viewModel.aksaraList
    val isLoading by viewModel.isLoading

    var expandedAksara by remember { mutableStateOf(false) }
    var selectedAksara by remember { mutableStateOf<Aksara?>(null) }

    // State untuk Dialog
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
        // --- 1. TOP BAR RAMPING & STICKY ---
        // Menggunakan CenterAlignedTopAppBar agar terlihat seperti aplikasi iOS/Android Modern
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Eksplorasi Aksara",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BrownDusk // Warna cokelat konsisten
                ),
                // Membuat lengkungan halus di bawah TopBar
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
                    containerColor = BrownDusk,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    icon = { Icon(Icons.Default.Add, null) },
                    text = { Text("Tambah", fontFamily = Poppins) }
                )
            }
        },
        containerColor = Color(0xFFF8F9FA) // Background abu-abu sangat muda agar Card putih terlihat kontras
    ) { innerPadding -> // FIX: Menghilangkan garis merah innerPadding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Konten akan otomatis mulai DI BAWAH topBar
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // --- 2. DROPDOWN SELECTION ---
            Text(
                "Pilih Jenis Aksara",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = BrownDusk,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expandedAksara,
                onExpandedChange = { expandedAksara = !expandedAksara }
            ) {
                OutlinedTextField(
                    value = selectedAksara?.nama ?: "Pilih Aksara Nusantara",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAksara) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrownDusk,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    textStyle = TextStyle(fontFamily = Poppins)
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

            // --- 3. LIST KONTEN PANDUAN ---
            if (selectedAksara == null) {
                // Tampilan saat belum memilih aksara
                EmptyStateView(Poppins)
            } else if (isLoading) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BrownDusk)
                }
            } else {
                panduanItems.forEach { item ->
                    // CARD MODERN
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        shape = RoundedCornerShape(28.dp), // Lebih bulat agar lebih modern
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Karakter: ${item.karakterTarget}",
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = BrownDusk
                                )
                                // Badge kecil untuk estetika
                                Surface(
                                    color = BrownDusk.copy(alpha = 0.1f),
                                    shape = CircleShape
                                ) {
                                    Text(
                                        "Panduan",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        fontSize = 10.sp,
                                        color = BrownDusk,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Kontainer GIF dengan Border halus
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFFF1F1F1)),
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
                                color = Color.DarkGray,
                                lineHeight = 20.sp
                            )

                            HorizontalDivider(Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)

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
                                    Text("Ubah", color = BrownDusk, fontFamily = Poppins, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = { viewModel.hapusPanduan(item.idPanduan, item.idAksara) },
                                    modifier = Modifier.background(Color(0xFFFFEBEE), CircleShape).size(36.dp)
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

    // --- DIALOG PENAMBAHAN ---
    if (showDialog) {
        // ... (Logika Dialog Sama Seperti Sebelumnya) ...
    }
}

@Composable
fun EmptyStateView(font: FontFamily) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Info, null, tint = Color.LightGray, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Pilih aksara di atas untuk\nmelihat panduan menulis.",
            color = Color.Gray,
            fontFamily = font,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
    }
}