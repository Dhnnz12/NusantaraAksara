package com.example.nusantaraaksara.ui.theme.screens



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.EarthySand
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAksaraScreen(viewModel: AksaraViewModel, onBack: () -> Unit) {
    var nama by remember { mutableStateOf("") }
    var asal by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var urlGambar by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tambah Aksara", fontWeight = FontWeight.Bold, color = GoldenHeritage) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = GoldenHeritage)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrownDusk)
            )
        },
        containerColor = Color(0xFFFDFBF9)
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CustomTextField(value = nama, onValueChange = { nama = it }, label = "Nama Aksara")
                    CustomTextField(value = asal, onValueChange = { asal = it }, label = "Asal Daerah")
                    CustomTextField(value = urlGambar, onValueChange = { urlGambar = it }, label = "URL Gambar")

                    OutlinedTextField(
                        value = deskripsi,
                        onValueChange = { deskripsi = it },
                        label = { Text("Deskripsi") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrownDusk)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.tambahAksara(nama, deskripsi, asal, urlGambar) { onBack() } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrownDusk),
                shape = RoundedCornerShape(16.dp),
                enabled = nama.isNotEmpty() && asal.isNotEmpty()
            ) {
                Text("Simpan Aksara", color = GoldenHeritage, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrownDusk, unfocusedContainerColor = Color(0xFFF8F8F8))
    )
}