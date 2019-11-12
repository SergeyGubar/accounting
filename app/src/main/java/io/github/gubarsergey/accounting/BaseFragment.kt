package io.github.gubarsergey.accounting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class BaseFragment<Props>: Fragment() {

    abstract val layout: Int
    abstract fun getProps(): LiveData<Props>
    abstract fun render(props: Props)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProps().observe(viewLifecycleOwner, Observer { props ->
            render(props)
        })
    }
}