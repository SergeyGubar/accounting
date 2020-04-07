package io.github.gubarsergey.accounting.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentMainBinding

class MainFragment : BaseFragment<MainFragment.Props, FragmentMainBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)

    data class Props(
        val records: List<Record>
    ) {
        data class Record(
            val id: String,
            val amount: Int,
            val category: String
        )
    }

    private val adapter = AccountanceRecyclerAdapter()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recordsRecycler.adapter = adapter
        binding.recordsRecycler.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(
            listOf(
                Props.Record("1", -64, "Some category"),
                Props.Record("2", +60, "Some category2"),
                Props.Record("3", -65, "Some category3")
            )
        )
    }
}
