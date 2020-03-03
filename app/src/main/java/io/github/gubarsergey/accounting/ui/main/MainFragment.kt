package io.github.gubarsergey.accounting.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : BaseFragment<MainFragment.Props>() {

    data class Props(
        val records: List<Record>
    ) {
        data class Record(
            val id: String,
            val amount: Int,
            val category: String
        )
    }

    //    private val viewModel: MainConnector by inject()
    private val adapter = AccountanceRecyclerAdapter()
    private lateinit var fragmentMainBinding: FragmentMainBinding

    override val layout: Int = R.layout.fragment_main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        return fragmentMainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMainBinding.recordsRecycler.adapter = adapter
        fragmentMainBinding.recordsRecycler.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(
            listOf(
                Props.Record("1", -64, "Some category"),
                Props.Record("2", +60, "Some category2"),
                Props.Record("3", -65, "Some category3")
            )
        )
    }

    override fun render(props: Props) {
        Timber.d("render: $props")
    }

}
