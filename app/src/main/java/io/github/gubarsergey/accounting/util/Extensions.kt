package io.github.gubarsergey.accounting.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


inline fun FragmentManager.transaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

val Context.inflater get() = LayoutInflater.from(this)

fun EditText.addSimpleTextChangeListener(listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener.invoke(s.toString())
        }

    })
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