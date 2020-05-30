package io.github.gubarsergey.accounting.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.databinding.FragmentTotalReportBinding
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.math.RoundingMode
import java.text.DecimalFormat

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
            val countSpent: Int,
            val currency: String,
            val usdCourse: Double,
            val eurCourse: Double
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
        Timber.d("render $props")
        with(binding) {
            val totalEarned = props.reports.fold(0.0, { a, b ->
                when {
                    b.currency.equals("usd", true) -> a + b.totalEarned * b.usdCourse
                    b.currency.equals("eur", true) -> a + b.totalEarned * b.eurCourse
                    else -> a + b.totalEarned
                }
            })
            val totalSpent = props.reports.fold(0.0, { a, b ->
                when {
                    b.currency.equals("usd", true) -> a + b.totalSpent * b.usdCourse
                    b.currency.equals("eur", true) -> a + b.totalSpent * b.eurCourse
                    else -> a + b.totalEarned
                }
            })

            val totalEarnedNumberOfTransactions = props.reports.sumBy { it.countEarned }
            val totalSpentNumberOfTransactions = props.reports.sumBy { it.countSpent }


            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING

            totalReportEarnedTextView.text = getString(
                R.string.for_all_time_you_earned_template,
                df.format(totalEarned),
                totalEarnedNumberOfTransactions.toString()
            )
            totalReportSpentTextView.text = getString(
                R.string.for_all_time_you_spent_template,
                df.format(totalSpent),
                totalSpentNumberOfTransactions.toString()
            )
        }
        adapter.submitList(props.reports)
    }
}