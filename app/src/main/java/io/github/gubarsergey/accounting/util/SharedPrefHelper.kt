package io.github.gubarsergey.accounting.util

import android.content.Context
import androidx.core.content.edit

private const val SHARED_PREF_TOKEN_KEY = "SHARED_PREF_TOKEN_KEY"

class SharedPrefHelper {

    fun clearToken(context: Context) {
        context.defaultSharedPreferences.edit {
            putString(SHARED_PREF_TOKEN_KEY, null)
        }
    }

    fun saveToken(context: Context, token: String) {
        context.defaultSharedPreferences.edit {
            putString(SHARED_PREF_TOKEN_KEY, token)
        }
    }

    fun getToken(context: Context): String? {
        return context.defaultSharedPreferences.getString(SHARED_PREF_TOKEN_KEY, null)
    }
}