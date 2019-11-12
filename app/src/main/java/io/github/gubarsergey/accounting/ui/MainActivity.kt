package io.github.gubarsergey.accounting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.ui.login.LoginFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
    }
}
