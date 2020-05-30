package io.github.gubarsergey.accounting.ui.report

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.databinding.ItemAllTimeReportBinding
import io.github.gubarsergey.accounting.ui.report.AllTimeReportFragment.Props.AccountReport
import io.github.gubarsergey.accounting.util.inflater
import io.github.gubarsergey.accounting.util.makeGone
import io.github.gubarsergey.accounting.util.makeVisible

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
            root.setOnClickListener {
                if (contentContainer.isVisible) {
                    TransitionManager.beginDelayedTransition(root, AutoTransition())
                    contentContainer.makeGone()
                } else {
                    TransitionManager.beginDelayedTransition(root, AutoTransition())
                    contentContainer.makeVisible()
                }
            }
            cardTitleTextView.text = item.title
            cardTotalEarnedTextView.text = itemView.context.getString(
                R.string.for_card_you_earned_template,
                item.title,
                item.totalEarned.toString(),
                item.currency,
                item.countEarned.toString()
            )
            cardTotalSpentTextView.text = itemView.context.getString(
                R.string.for_card_you_lost_template,
                item.totalSpent.toString(),
                item.currency,
                item.countSpent.toString()
            )
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