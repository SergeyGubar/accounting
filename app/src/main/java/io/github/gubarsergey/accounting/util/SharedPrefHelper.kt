package io.github.gubarsergey.accounting.util

import android.content.Context
import androidx.core.content.edit
import arrow.core.Option

private const val SHARED_PREF_TOKEN_KEY = "SHARED_PREF_TOKEN_KEY"

fun saveToken(context: Context, token: String) {
//    context.defaultSharedPreferences.edit {
//        putString(SHARED_PREF_TOKEN_KEY, token)
//    }
}

fun getToken(context: Context): Option<String> {
    val token = context.defaultSharedPreferences.getString(SHARED_PREF_TOKEN_KEY, null)
    return if (token == null) Option.empty() else Option.just(token)
}