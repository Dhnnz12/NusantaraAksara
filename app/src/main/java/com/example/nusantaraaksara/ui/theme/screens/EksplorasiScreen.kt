package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.nusantaraaksara.model.PanduanPenulisan
import com.example.nusantaraaksara.ui.theme.*
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EksplorasiScreen(
    viewModel: AksaraViewModel,
    settingsViewModel: SettingsViewModel,
    isAdmin: Boolean = true, // Sesuaikan dengan login session nantinya
    onBack: () -> Boolean
) {
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    val aksaraList by viewModel.aksaraList
    val panduanItems by viewModel.panduanList
    val isLoading by viewModel.isLoading

    var expandedAksara by remember { mutableStateOf(false) }
    var selectedAksara by remember { mutableStateOf<Aksara?>(null) }

    // State untuk Dialog Form
    var showDialog by remember { mutableStateOf(false) }
    var editingPanduan by remember { mutableStateOf<PanduanPenulisan?>(null) }

    val Poppins = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_regular, FontWeight.Normal)
    )

    LaunchedEffect(Unit) { viewModel.ambilSemuaAksara() }
    LaunchedEffect(selectedAksara) {
        selectedAksara?.let { viewModel.loadPanduan(it.id) }
    }

    val bgColor = if (isDark) Color(0xFF121212) else Color.White
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White

    Scaffold(
        containerColor = bgColor,
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().height(130.dp),
                color = if (isDark) Color(0xFF1A1614) else BrownDusk,
                shape = RoundedCornerShape(bottomStart = 38.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(start = 28.dp,top= 8.dp), verticalArrangement = Arrangement.Center) {
                    Text(text = if (currentLang == "id") "Eksplorasi." else "Explore.", color = GoldenHeritage, fontSize = 32.sp, fontWeight = FontWeight.Black, fontFamily = Poppins)
                    Text(text = if (currentLang == "id") "Panduan Aksara Nusantara" else "Nusantara Script Guide", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp, fontFamily = Poppins)
                }
            }
        },
        floatingActionButton = {
            if (isAdmin && selectedAksara != null) {
                FloatingActionButton(
                    onClick = {
                        editingPanduan = null
                        showDialog = true
                    },
                    containerColor = GoldenHeritage,
                    contentColor = Color.White
                ) { Icon(Icons.Default.Add, "Tambah Panduan") }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(24.dp)
        ) {
            Text(text = if (currentLang == "id") "Pilih Jenis Aksara" else "Select Script Type", fontFamily = Poppins, fontWeight = FontWeight.Bold, color = if (isDark) GoldenHeritage else BrownDusk, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(expanded = expandedAksara, onExpandedChange = { expandedAksara = !expandedAksara }) {
                OutlinedTextField(
                    value = selectedAksara?.nama ?: "Pilih Aksara",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAksara) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(fontFamily = Poppins, fontSize = 14.sp)
                )
                ExposedDropdownMenu(expanded = expandedAksara, onDismissRequest = { expandedAksara = false }) {
                    aksaraList.forEach { aksara ->
                        DropdownMenuItem(text = { Text(aksara.nama, fontFamily = Poppins) }, onClick = { selectedAksara = aksara; expandedAksara = false })
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (selectedAksara == null) {
                EmptyStateView(Poppins, currentLang, isDark)
            } else if (isLoading) {
                Box(Modifier.fillMaxWidth().padding(top = 50.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = GoldenHeritage) }
            } else {
                panduanItems.forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Karakter: ${item.karakterTarget}", fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = if(isDark) GoldenHeritage else BrownDusk)

                                if (isAdmin) {
                                    Row {
                                        IconButton(onClick = {
                                            editingPanduan = item
                                            showDialog = true
                                        }) { Icon(Icons.Default.Edit, "Edit", tint = Color(0xFF2196F3)) }
                                        IconButton(onClick = { viewModel.hapusPanduan(item.idPanduan, selectedAksara!!.id) }) {
                                            Icon(Icons.Default.Delete, "Hapus", tint = Color(0xFFF44336))
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(20.dp)).background(if(isDark) Color(0xFF2D2D2D) else Color(0xFFF5F5F5)), contentAlignment = Alignment.Center) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current).data(item.fileAnimasi).decoderFactory(GifDecoder.Factory()).build(),
                                    contentDescription = null, modifier = Modifier.size(140.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = item.urutanLangkah, fontFamily = Poppins, fontSize = 14.sp, color = if(isDark) Color.LightGray else Color.DarkGray, lineHeight = 20.sp)
                        }
                    }
                }
            }
        }
    }

    // --- FORM DIALOG (TAMBAH / EDIT) ---
    if (showDialog) {
        PanduanFormDialog(
            panduan = editingPanduan,
            idAksara = selectedAksara?.id ?: 0,
            onDismiss = { showDialog = false },
            onSave = { karakter, animasi, langkah ->
                if (editingPanduan == null) {
                    viewModel.tambahPanduan(selectedAksara!!.id, karakter, animasi, langkah) { showDialog = false }
                } else {
                    viewModel.updatePanduan(editingPanduan!!.idPanduan, selectedAksara!!.id, karakter, animasi, langkah) { showDialog = false }
                }
            },
            Poppins = Poppins
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanduanFormDialog(
    panduan: PanduanPenulisan?,
    idAksara: Int,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit,
    Poppins: FontFamily
) {
    var karakter by remember { mutableStateOf(panduan?.karakterTarget ?: "") }
    var animasi by remember { mutableStateOf(panduan?.fileAnimasi ?: "") }
    var langkah by remember { mutableStateOf(panduan?.urutanLangkah ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (panduan == null) "Tambah Panduan" else "Edit Panduan", fontFamily = Poppins, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = karakter, onValueChange = { karakter = it }, label = { Text("Karakter (Contoh: Ha)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = animasi, onValueChange = { animasi = it }, label = { Text("URL Animasi GIF") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = langkah, onValueChange = { langkah = it }, label = { Text("Urutan Langkah") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(karakter, animasi, langkah) },
                colors = ButtonDefaults.buttonColors(containerColor = GoldenHeritage)
            ) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal", color = Color.Gray) }
        }
    )
}

@Composable
fun EmptyStateView(font: FontFamily, lang: String, isDark: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF1F1F1)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Info, null, tint = Color.Gray, modifier = Modifier.size(32.dp))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = if (lang == "id") "Pilih aksara di atas untuk\nmulai eksplorasi panduan."
            else "Select a script above to\nstart exploring guides.",
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontFamily = font,
            fontSize = 14.sp
        )
    }
}