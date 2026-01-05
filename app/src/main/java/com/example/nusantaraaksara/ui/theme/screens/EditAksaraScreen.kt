package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.EarthySand
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAksaraScreen(aksara: Aksara, viewModel: AksaraViewModel, onBack: () -> Unit) {
    var nama by remember { mutableStateOf(aksara.nama) }
    var asal by remember { mutableStateOf(aksara.asal_daerah) }
    var deskripsi by remember { mutableStateOf(aksara.deskripsi) }
    var urlGambar by remember { mutableStateOf(aksara.gambar_url) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Edit Aksara",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BrownDusk),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            )
        },
        containerColor = EarthySand
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
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomTextField(value = nama, onValueChange = { nama = it }, label = "Nama Aksara")
                    CustomTextField(value = asal, onValueChange = { asal = it }, label = "Asal Daerah")
                    CustomTextField(value = urlGambar, onValueChange = { urlGambar = it }, label = "URL Gambar")

                    Column {
                        Text(
                            "Deskripsi",
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrownDusk,
                            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = deskripsi,
                            onValueChange = { deskripsi = it },
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            textStyle = TextStyle(fontFamily = Poppins),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrownDusk,
                                unfocusedBorderColor = Color(0xFFEEEEEE)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.updateAksara(aksara.id, nama, deskripsi, asal, urlGambar) { onBack() } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrownDusk),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    "SIMPAN PERUBAHAN",
                    fontFamily = Poppins,
                    color = GoldenHeritage,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}