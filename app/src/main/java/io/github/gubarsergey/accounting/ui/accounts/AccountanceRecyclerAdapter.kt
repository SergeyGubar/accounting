package io.github.gubarsergey.accounting.ui.accounts

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.gubarsergey.accounting.BaseViewHolder
import io.github.gubarsergey.accounting.databinding.ItemRecordBinding
import io.github.gubarsergey.accounting.util.inflater

class AccountanceRecyclerAdapter :
    ListAdapter<AccountsPagerAdapter.Props.AccountInfo.Transaction, AccountanceRecyclerAdapter.RecordViewHolder>(
        RecordsDiffUtilCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder =
        RecordViewHolder(ItemRecordBinding.inflate(parent.context.inflater, parent, false))

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class RecordViewHolder(private val recordBinding: ItemRecordBinding) :
        BaseViewHolder<AccountsPagerAdapter.Props.AccountInfo.Transaction>(recordBinding.root) {
        override fun render(item: AccountsPagerAdapter.Props.AccountInfo.Transaction) {
            recordBinding.itemRecordAmountTextView.text = item.amount.toString()
            recordBinding.itemRecordCategoryTextView.text = item.category
        }
    }

    class RecordsDiffUtilCallback :
        DiffUtil.ItemCallback<AccountsPagerAdapter.Props.AccountInfo.Transaction>() {
        override fun areItemsTheSame(
            oldItem: AccountsPagerAdapter.Props.AccountInfo.Transaction,
            newItem: AccountsPagerAdapter.Props.AccountInfo.Transaction
        ): Boolean = oldItem.id == newItem.id


        override fun areContentsTheSame(
            oldItem: AccountsPagerAdapter.Props.AccountInfo.Transaction,
            newItem: AccountsPagerAdapter.Props.AccountInfo.Transaction
        ): Boolean {
            return oldItem.amount == newItem.amount &&
                    oldItem.category == newItem.category
        }

    }
}