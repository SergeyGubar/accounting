package io.github.gubarsergey.accounting.ui.transaction.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.gubarsergey.accounting.BaseViewHolder
import io.github.gubarsergey.accounting.databinding.ItemRecordBinding
import io.github.gubarsergey.accounting.util.inflater

class TransactionsRecyclerAdapter :
    ListAdapter<TransactionsPagerAdapter.Props.AccountInfo.Transaction, TransactionsRecyclerAdapter.RecordViewHolder>(
        RecordsDiffUtilCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder =
        RecordViewHolder(ItemRecordBinding.inflate(parent.context.inflater, parent, false))

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class RecordViewHolder(private val recordBinding: ItemRecordBinding) :
        BaseViewHolder<TransactionsPagerAdapter.Props.AccountInfo.Transaction>(recordBinding.root) {
        override fun render(item: TransactionsPagerAdapter.Props.AccountInfo.Transaction) {
            recordBinding.itemRecordAmountTextView.text = item.amount.toString()
            recordBinding.itemRecordCategoryTextView.text = item.category
        }
    }

    class RecordsDiffUtilCallback :
        DiffUtil.ItemCallback<TransactionsPagerAdapter.Props.AccountInfo.Transaction>() {
        override fun areItemsTheSame(
            oldItem: TransactionsPagerAdapter.Props.AccountInfo.Transaction,
            newItem: TransactionsPagerAdapter.Props.AccountInfo.Transaction
        ): Boolean = oldItem.id == newItem.id


        override fun areContentsTheSame(
            oldItem: TransactionsPagerAdapter.Props.AccountInfo.Transaction,
            newItem: TransactionsPagerAdapter.Props.AccountInfo.Transaction
        ): Boolean {
            return oldItem.amount == newItem.amount &&
                    oldItem.category == newItem.category
        }

    }
}