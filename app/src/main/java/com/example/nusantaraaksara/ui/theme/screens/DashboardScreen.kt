package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.nusantaraaksara.R
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.EarthySand
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    aksaraViewModel: AksaraViewModel,
    authViewModel: AuthViewModel,
    onAksaraClick: (Aksara) -> Unit,
    onAddAksaraClick: () -> Unit,
    onProfileClick: () -> Unit,
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val aksaraState by aksaraViewModel.aksaraList
    val authState by authViewModel.state
    val userName = authState.user?.username ?: "Pengguna"

    val Poppins = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_extra_bold, FontWeight.ExtraBold),
        Font(R.font.poppins_medium, FontWeight.Medium)
    )

    LaunchedEffect(Unit) {
        aksaraViewModel.ambilSemuaAksara()
    }

    Scaffold(
        // Kita hapus topBar bawaan jika ada agar tidak menambah space
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddAksaraClick,
                containerColor = BrownDusk,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Tambah", fontFamily = Poppins, fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        // SOLUSI: Jangan gunakan .padding(innerPadding) di Box terluar
        // karena itu yang menarik konten ke bawah (bikin space putih)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // --- HEADER (Dibuat mepet ke atas) ---
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp), // Sedikit lebih tinggi untuk menutupi area status bar
                color = BrownDusk,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            ) {
                // Gunakan padding top manual untuk memberi jarak dari notch/kamera
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .padding(top = 20.dp), // Jarak aman agar teks tidak tertutup kamera
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Sugeng Rawuh,",
                            color = EarthySand.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            fontFamily = Poppins
                        )
                        Text(
                            text = userName,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins
                        )
                    }

                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.15f), CircleShape)
                            .size(45.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }

            // --- LIST KONTEN ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    // innerPadding.calculateTopPadding() kita abaikan, ganti manual
                    top = 155.dp,
                    start = 20.dp,
                    end = 20.dp,
                    // Tetap gunakan padding bawah agar FAB tidak menutupi item terakhir
                    bottom = innerPadding.calculateBottomPadding() + 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Koleksi Aksara",
                        fontSize = 20.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        color = BrownDusk
                    )
                }

                items(aksaraState) { aksara ->
                    AksaraItemCard(aksara, Poppins, { onAksaraClick(aksara) }, { aksaraViewModel.hapusAksara(aksara.id) })
                }
            }
        }
    }
}

@Composable
fun AksaraItemCard(aksara: Aksara, Poppins: FontFamily, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF5F5F5),
                modifier = Modifier.size(60.dp)
            ) {
                AsyncImage(
                    model = aksara.gambar_url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(aksara.nama, fontSize = 17.sp, fontFamily = Poppins, fontWeight = FontWeight.Bold)
                Text(aksara.asal_daerah, fontSize = 13.sp, fontFamily = Poppins, color = Color.Gray)
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.background(Color(0xFFFFF2F2), CircleShape).size(32.dp)
            ) {
                Icon(Icons.Default.Delete, null, tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
            }
        }
    }
}