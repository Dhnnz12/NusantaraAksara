package com.example.nusantaraaksara.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        const val USER_ID = "user_id"
        const val USERNAME = "username"
        const val IS_LOGGED_IN = "is_logged_in"
    }

    fun saveSession(id: Int, username: String) {
        val editor = prefs.edit()
        editor.putInt(USER_ID, id)
        editor.putString(USERNAME, username)
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.apply()
    }

    fun getUserId(): Int = prefs.getInt(USER_ID, -1)

    fun logout() {
        prefs.edit().clear().apply()
    }
}