package io.github.gubarsergey.accounting.ui.accounts

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.gubarsergey.accounting.BaseViewHolder
import io.github.gubarsergey.accounting.databinding.ItemRecordBinding
import io.github.gubarsergey.accounting.util.inflater

class AccountanceRecyclerAdapter :
    ListAdapter<AccountsFragment.Props.Transaction, AccountanceRecyclerAdapter.RecordViewHolder>(
        RecordsDiffUtilCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder =
        RecordViewHolder(ItemRecordBinding.inflate(parent.context.inflater, parent, false))

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class RecordViewHolder(private val recordBinding: ItemRecordBinding) :
        BaseViewHolder<AccountsFragment.Props.Transaction>(recordBinding.root) {
        override fun render(item: AccountsFragment.Props.Transaction) {
            recordBinding.itemRecordAmountTextView.text = item.amount.toString()
            recordBinding.itemRecordCategoryTextView.text = item.category
        }
    }

    class RecordsDiffUtilCallback : DiffUtil.ItemCallback<AccountsFragment.Props.Transaction>() {
        override fun areItemsTheSame(
            oldItem: AccountsFragment.Props.Transaction,
            newItem: AccountsFragment.Props.Transaction
        ): Boolean = oldItem.id == newItem.id


        override fun areContentsTheSame(
            oldItem: AccountsFragment.Props.Transaction,
            newItem: AccountsFragment.Props.Transaction
        ): Boolean {
            return oldItem.amount == newItem.amount &&
                    oldItem.category == newItem.category
        }

    }
}