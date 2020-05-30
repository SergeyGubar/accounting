package io.github.gubarsergey.accounting.ui.report

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.gubarsergey.accounting.databinding.ItemAllTimeReportBinding
import io.github.gubarsergey.accounting.ui.report.AllTimeReportFragment.Props.AccountReport
import io.github.gubarsergey.accounting.util.inflater

class AllTimeReportRecyclerAdapter :
    ListAdapter<AccountReport, AllTimeReportRecyclerAdapter.AllTimeReportViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTimeReportViewHolder =
        AllTimeReportViewHolder(
            ItemAllTimeReportBinding.inflate(
                parent.context.inflater,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: AllTimeReportViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AllTimeReportViewHolder(private val binding: ItemAllTimeReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AccountReport) = with(binding) {
            cardTitleTextView.text = item.title
            cardTotalEarnedTextView.text = item.totalEarned.toString()
            cardTotalSpentTextView.text = item.totalSpent.toString()
            cardTotalNumberOfTransactionsTextView.text = item.countEarned.toString()
        }
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AccountReport>() {
    override fun areItemsTheSame(
        oldItem: AccountReport,
        newItem: AccountReport
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: AccountReport,
        newItem: AccountReport
    ): Boolean = oldItem == newItem

}