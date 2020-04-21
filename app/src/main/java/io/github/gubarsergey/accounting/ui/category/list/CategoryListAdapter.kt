package io.github.gubarsergey.accounting.ui.category.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.gubarsergey.accounting.databinding.ItemCategoryBinding
import io.github.gubarsergey.accounting.util.inflater

class CategoryListAdapter :
    ListAdapter<CategoryListFragment.Props.Category, CategoryListAdapter.CategoryViewHolder>(
        CategoryDiffCallback()
    ) {

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryListFragment.Props.Category) {
            binding.itemCategoryIdTextView.text = item.id.subSequence(0, 6)
            binding.itemCategoryNameTextView.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            ItemCategoryBinding.inflate(parent.context.inflater, parent, false)
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryListFragment.Props.Category>() {
    override fun areItemsTheSame(
        oldItem: CategoryListFragment.Props.Category,
        newItem: CategoryListFragment.Props.Category
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: CategoryListFragment.Props.Category,
        newItem: CategoryListFragment.Props.Category
    ): Boolean = oldItem == newItem

}