package com.example.nusantaraaksara.ui.theme.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
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

    // State Form Input
    var karakterTarget by remember { mutableStateOf("") }
    var fileAnimasi by remember { mutableStateOf("") }
    var urutanLangkah by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.ambilSemuaAksara()
    }

    LaunchedEffect(selectedAksara) {
        selectedAksara?.let {
            viewModel.loadPanduan(it.id)
        }
    }

    Scaffold(
        floatingActionButton = {
            // Hanya munculkan tombol tambah jika Aksara sudah dipilih
            if (selectedAksara != null) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        isEditMode = false
                        karakterTarget = ""; fileAnimasi = ""; urutanLangkah = ""
                        showDialog = true
                    }
                ) {
                    // Ganti '医疗' menjadi 'tint'
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah",
                        tint = Color.White // Parameter yang benar adalah 'tint'
                    )
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {

            Text("Pilih Aksara Terlebih Dahulu", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            // DROPDOWN
            ExposedDropdownMenuBox(
                expanded = expandedAksara,
                onExpandedChange = { expandedAksara = !expandedAksara }
            ) {
                OutlinedTextField(
                    value = selectedAksara?.nama ?: "Pilih Jenis Aksara",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAksara) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedAksara,
                    onDismissRequest = { expandedAksara = false }
                ) {
                    aksaraList.forEach { aksara ->
                        DropdownMenuItem(
                            text = { Text(aksara.nama) },
                            onClick = {
                                selectedAksara = aksara
                                expandedAksara = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedAksara == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Silakan pilih aksara untuk mengelola panduan", color = Color.Gray)
                }
            } else if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(panduanItems) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Karakter: ${item.karakterTarget}", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                                Spacer(modifier = Modifier.height(8.dp))

                                // Preview Animasi
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(item.fileAnimasi)
                                        .decoderFactory(GifDecoder.Factory()).build(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Langkah: ${item.urutanLangkah}", style = MaterialTheme.typography.bodyMedium)

                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp)
                                ) {
                                    FilledTonalButton(
                                        onClick = {
                                            selectedIdPanduan = item.idPanduan
                                            karakterTarget = item.karakterTarget
                                            fileAnimasi = item.fileAnimasi
                                            urutanLangkah = item.urutanLangkah
                                            isEditMode = true
                                            showDialog = true
                                        },
                                        modifier = Modifier.padding(end = 8.dp)
                                    ) { Text("Edit") }

                                    Button(
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                                        onClick = { viewModel.hapusPanduan(item.idPanduan, item.idAksara) }
                                    ) { Text("Hapus", color = Color.White) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // DIALOG TAMBAH / EDIT
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (isEditMode) "Update Panduan" else "Tambah Panduan ${selectedAksara?.nama}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = karakterTarget, onValueChange = { karakterTarget = it }, label = { Text("Karakter Target (Contoh: Ka)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = fileAnimasi, onValueChange = { fileAnimasi = it }, label = { Text("URL GIF Animasi") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = urutanLangkah, onValueChange = { urutanLangkah = it }, label = { Text("Deskripsi Langkah") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                }
            },
            confirmButton = {
                Button(onClick = {
                    selectedAksara?.let { aksara ->
                        if (isEditMode) {
                            viewModel.updatePanduan(selectedIdPanduan, aksara.id, karakterTarget, fileAnimasi, urutanLangkah) { showDialog = false }
                        } else {
                            viewModel.tambahPanduan(aksara.id, karakterTarget, fileAnimasi, urutanLangkah) { showDialog = false }
                        }
                    }
                }) { Text("Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Batal") }
            }
        )
    }
}
