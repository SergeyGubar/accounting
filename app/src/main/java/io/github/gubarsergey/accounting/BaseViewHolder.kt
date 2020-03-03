package io.github.gubarsergey.accounting

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(
    view: View,
    private val isItemClickEnabled: Boolean = false,
    private val itemClickHandler: (T) -> Unit = { },
    private val isItemLongClickEnabled: Boolean = false,
    private val itemLongClickHandler: (T) -> Unit = { }
) : RecyclerView.ViewHolder(view) {
    fun bind(item: T) {
        if (isItemClickEnabled) {
            itemView.setOnClickListener {
                itemClickHandler(item)
            }
        }
        if (isItemLongClickEnabled) {
            itemView.setOnLongClickListener {
                itemLongClickHandler(item)
                false
            }
        }
        render(item)
    }

    protected abstract fun render(item: T)
}