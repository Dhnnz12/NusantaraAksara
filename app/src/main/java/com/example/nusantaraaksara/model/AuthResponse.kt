package com.example.nusantaraaksara.model

data class LoginResponse(
    val message: String,
    val token: String,
    val user: User,
  val userData: UserData
)

data class UserData(
    val id: Int,
    val username: String,
    val email: String
)