package io.github.gubarsergey.accounting.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentTotalReportBinding
import org.koin.android.ext.android.inject

class AllTimeReportFragment : BaseFragment<FragmentTotalReportBinding>() {

    private val interactor: AllTimeReportInteractor by inject()
    private val adapter: AllTimeReportRecyclerAdapter = AllTimeReportRecyclerAdapter()

    data class Props(
        val reports: List<AccountReport>
    ) {
        data class AccountReport(
            val id: String,
            val title: String,
            val totalEarned: Int,
            val countEarned: Int,
            val totalSpent: Int,
            val countSpent: Int
        )
    }


    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTotalReportBinding = FragmentTotalReportBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        interactor.submit(AllTimeReportInteractor.AllTimeReportIntent.LoadReport)
        interactor.props.observe(viewLifecycleOwner, Observer {
            render(it)
        })

        binding.cardReportsRecycler.adapter = adapter
        binding.cardReportsRecycler.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun render(props: Props) {
        adapter.submitList(props.reports)
    }
}