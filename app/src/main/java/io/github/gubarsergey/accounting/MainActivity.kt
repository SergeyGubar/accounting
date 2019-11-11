package io.github.gubarsergey.accounting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.gubarsergey.accounting.ui.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.main_container, LoginFragment()).commit()
    }
}
