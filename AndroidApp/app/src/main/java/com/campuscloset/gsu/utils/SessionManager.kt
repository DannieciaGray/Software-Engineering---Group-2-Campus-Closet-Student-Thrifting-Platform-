package com.campuscloset.gsu.utils

import android.content.Context

/**
 * Matches the SharedPreferences setup in LoginActivity exactly:
 *   prefs name = "session"
 *   key = "userId" stored as Int
 */
object SessionManager {

    private const val PREF_NAME = "session"
    private const val KEY_USER_ID = "userId"

    fun getUserId(context: Context): Int =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_USER_ID, -1)

    fun isLoggedIn(context: Context): Boolean = getUserId(context) != -1

    fun clearSession(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().clear().apply()
    }
}
