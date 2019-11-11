package io.github.gubarsergey.accounting

import androidx.fragment.app.Fragment

abstract class BaseFragment<Props>: Fragment() {
    abstract fun render(props: Props)
}