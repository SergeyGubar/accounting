package io.github.gubarsergey.accounting.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import io.github.gubarsergey.accounting.redux.Consumer

private const val SHARED_PREF_NAME = "accounting-shared-pref"

inline fun FragmentManager.transaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

val Context.inflater get() = LayoutInflater.from(this)

val Context.defaultSharedPreferences
    get() = this.getSharedPreferences(
        SHARED_PREF_NAME,
        Context.MODE_PRIVATE
    )


fun EditText.addSimpleTextChangeListener(listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener.invoke(s.toString())
        }
    })
}

fun <T> MutableLiveData<T>.asConsumer(): Consumer<T> {
    return { t -> this.postValue(t) }
}

fun EditText.safelySetText(text: String) {
    if (this.text?.toString() != text) {
        this.setText(text)
    }
}

fun View.setViewEnabled() {
    this.isEnabled = true
}

fun View.setViewDisabled() {
    this.isEnabled = true
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun Fragment.snackbar(text: String) {
    Snackbar.make(this.view!!, text, Snackbar.LENGTH_SHORT).show()
}

fun Fragment.snackbar(@StringRes text: Int) {
    Snackbar.make(this.view!!, text, Snackbar.LENGTH_SHORT).show()
}