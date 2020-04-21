package io.github.gubarsergey.accounting.ui.category.total

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.gubarsergey.accounting.databinding.ItemCategoryTotalSpentBinding
import io.github.gubarsergey.accounting.util.inflater

class CategoryTotalSpentRecyclerAdapter :
    ListAdapter<CategoryTotalSpentFragment.Props.TotalSpent, CategoryTotalSpentRecyclerAdapter.CategoryTotalSpentViewHolder>(
        CategorySpentTotalDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryTotalSpentViewHolder = CategoryTotalSpentViewHolder(
        ItemCategoryTotalSpentBinding.inflate(
            parent.context.inflater,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: CategoryTotalSpentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoryTotalSpentViewHolder(private val binding: ItemCategoryTotalSpentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryTotalSpentFragment.Props.TotalSpent) = with(binding) {
            itemTotalSpentAmount.text = item.total.toString()
            itemTotalSpentCategoryName.text = item.categoryName
            itemTotalSpentNumberOfTransactions.text = item.transactionsCount.toString()
        }
    }
}

class CategorySpentTotalDiffCallback :
    DiffUtil.ItemCallback<CategoryTotalSpentFragment.Props.TotalSpent>() {
    override fun areItemsTheSame(
        oldItem: CategoryTotalSpentFragment.Props.TotalSpent,
        newItem: CategoryTotalSpentFragment.Props.TotalSpent
    ): Boolean = oldItem.categoryId == newItem.categoryId

    override fun areContentsTheSame(
        oldItem: CategoryTotalSpentFragment.Props.TotalSpent,
        newItem: CategoryTotalSpentFragment.Props.TotalSpent
    ): Boolean = oldItem == newItem

}