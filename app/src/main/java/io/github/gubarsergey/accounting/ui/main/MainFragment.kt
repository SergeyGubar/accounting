package io.github.gubarsergey.accounting.ui.main

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.R
import kotlinx.android.synthetic.main.fragment_main.*
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

    private val viewModel: MainViewModel by viewModels()
    private val adapter = AccountanceRecyclerAdapter()

    override val layout: Int = R.layout.fragment_main

    override fun getProps(): LiveData<Props> = viewModel.props


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        records_recycler.adapter = adapter
        records_recycler.layoutManager = LinearLayoutManager(requireContext())
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
