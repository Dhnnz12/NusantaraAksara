package com.example.nusantaraaksara.ui.theme


sealed class Screen(val route: String) {
    object Welcome : Screen("welcome") // Tambahkan ini
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")

    object Profile : Screen("profile")
    object ChangePassword : Screen("change_password")
    object About : Screen("about")
    object HelpFaq : Screen("help_faq")
    object DetailAksara : Screen("detail/{aksaraId}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}