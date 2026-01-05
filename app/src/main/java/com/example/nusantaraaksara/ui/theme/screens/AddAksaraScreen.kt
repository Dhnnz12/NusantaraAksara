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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                title = {
                    Text(
                        "Tambah Aksara",
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
        containerColor = EarthySand // Menggunakan variabel warna Anda
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
                            placeholder = { Text("Tulis deskripsi singkat...", color = Color.LightGray) },
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            textStyle = TextStyle(fontFamily = Poppins),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrownDusk,
                                unfocusedBorderColor = Color(0xFFEEEEEE),
                                focusedContainerColor = Color(0xFFFBFBFB)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.tambahAksara(nama, deskripsi, asal, urlGambar) { onBack() } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrownDusk),
                shape = RoundedCornerShape(18.dp),
                enabled = nama.isNotEmpty() && asal.isNotEmpty(),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    "SIMPAN AKSARA",
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
@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    Column {
        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = BrownDusk,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Masukkan $label", color = Color.LightGray, fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(fontFamily = Poppins, fontSize = 15.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrownDusk,
                unfocusedBorderColor = Color(0xFFEEEEEE),
                focusedContainerColor = Color(0xFFFBFBFB),
                unfocusedContainerColor = Color.White
            )
        )
    }
}