package io.github.gubarsergey.accounting.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.gubarsergey.accounting.BaseViewHolder
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.util.inflater
import kotlinx.android.synthetic.main.item_record.*

class AccountanceRecyclerAdapter :
    ListAdapter<MainFragment.Props.Record, AccountanceRecyclerAdapter.RecordViewHolder>(
        RecordsDiffUtilCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder =
        RecordViewHolder(
            parent.context.inflater.inflate(
                R.layout.item_record, parent, false
            )
        )

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class RecordViewHolder(view: View) : BaseViewHolder<MainFragment.Props.Record>(view) {
        override fun render(item: MainFragment.Props.Record) {
            item_record_amount_text_view.text = item.amount.toString()
            item_record_category_text_view.text = item.category
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