package io.github.gubarsergey.accounting.storybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.ui.login.LoginFragment
import io.github.gubarsergey.accounting.util.transaction

class StorybookActivity : AppCompatActivity(), StorybookActionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storybook)
        supportFragmentManager.transaction {
            add(R.id.storybook_container, StorybookContainer())
        }
    }

    override fun showLogin(props: LoginFragment.Props) {
        supportFragmentManager.transaction {
            replace(R.id.storybook_container, LoginFragment())
        }
    }
}
