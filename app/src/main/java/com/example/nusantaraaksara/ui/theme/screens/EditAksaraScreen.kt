package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAksaraScreen(
    aksara: Aksara, // Data aksara yang dipilih untuk diedit
    viewModel: AksaraViewModel,
    onBack: () -> Unit
) {
    var nama by remember { mutableStateOf(aksara.nama) }
    var asal by remember { mutableStateOf(aksara.asal_daerah) }
    var deskripsi by remember { mutableStateOf(aksara.deskripsi) }
    var urlGambar by remember { mutableStateOf(aksara.gambar_url) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Aksara", fontWeight = FontWeight.Bold, color = GoldenHeritage) },
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
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(20.dp)) {
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
                onClick = {
                    viewModel.updateAksara(aksara.id, nama, deskripsi, asal, urlGambar) { onBack() }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrownDusk),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Simpan Perubahan", color = GoldenHeritage, fontWeight = FontWeight.Bold)
            }
        }
    }
}