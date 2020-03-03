package io.github.gubarsergey.accounting.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.gubarsergey.accounting.BaseViewHolder
import io.github.gubarsergey.accounting.databinding.ItemRecordBinding
import io.github.gubarsergey.accounting.util.inflater

class AccountanceRecyclerAdapter :
    ListAdapter<MainFragment.Props.Record, AccountanceRecyclerAdapter.RecordViewHolder>(
        RecordsDiffUtilCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder =
        RecordViewHolder(ItemRecordBinding.inflate(parent.context.inflater, parent, false))

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class RecordViewHolder(private val recordBinding: ItemRecordBinding) :
        BaseViewHolder<MainFragment.Props.Record>(recordBinding.root) {
        override fun render(item: MainFragment.Props.Record) {
            recordBinding.itemRecordAmountTextView.text = item.amount.toString()
            recordBinding.itemRecordCategoryTextView.text = item.category
        }
    }

    class RecordsDiffUtilCallback : DiffUtil.ItemCallback<MainFragment.Props.Record>() {
        override fun areItemsTheSame(
            oldItem: MainFragment.Props.Record,
            newItem: MainFragment.Props.Record
        ): Boolean = oldItem.id == newItem.id


        override fun areContentsTheSame(
            oldItem: MainFragment.Props.Record,
            newItem: MainFragment.Props.Record
        ): Boolean {
            return oldItem.amount == newItem.amount &&
                    oldItem.category == newItem.category
        }

    }
}