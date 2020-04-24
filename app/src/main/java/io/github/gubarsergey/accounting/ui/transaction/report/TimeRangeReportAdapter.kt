package io.github.gubarsergey.accounting.ui.transaction.report

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.gubarsergey.accounting.databinding.ItemDailyReportBinding
import io.github.gubarsergey.accounting.util.inflater

class TimeRangeReportAdapter :
    ListAdapter<TimeRangeReportFragment.Props.DailySpent, TimeRangeReportAdapter.DailyReportViewHolder>(
        DailySpentDiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyReportViewHolder =
        DailyReportViewHolder(
            ItemDailyReportBinding.inflate(
                parent.context.inflater,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DailyReportViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DailyReportViewHolder(private val binding: ItemDailyReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimeRangeReportFragment.Props.DailySpent) = with(binding) {
            itemDailyReportNumberTextView.text = item.numberOfTransactions.toString()
            itemDailyReportDateTextView.text = item.createdAt
            itemDailyReportTotalAmountTextView.text = item.totalSpent.toString()
        }
    }
}

class DailySpentDiffUtilCallback :
    DiffUtil.ItemCallback<TimeRangeReportFragment.Props.DailySpent>() {
    override fun areItemsTheSame(
        oldItem: TimeRangeReportFragment.Props.DailySpent,
        newItem: TimeRangeReportFragment.Props.DailySpent
    ): Boolean = oldItem.createdAt == newItem.createdAt

    override fun areContentsTheSame(
        oldItem: TimeRangeReportFragment.Props.DailySpent,
        newItem: TimeRangeReportFragment.Props.DailySpent
    ): Boolean = oldItem == newItem

}