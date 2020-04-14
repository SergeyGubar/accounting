package io.github.gubarsergey.accounting.ui.transaction.list

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.gubarsergey.accounting.databinding.LayoutAccountBinding
import io.github.gubarsergey.accounting.util.inflater

class TransactionsPagerAdapter :
    RecyclerView.Adapter<TransactionsPagerAdapter.AccountPagerViewHolder>() {

    var props: Props =
        Props()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    data class Props(
        val accountsInfo: List<AccountInfo> = emptyList()
    ) {
        data class AccountInfo(
            val id: String,
            val accountTitle: String,
            val accountType: String,
            val transactions: List<Transaction>
        ) {
            data class Transaction(
                val id: String,
                val amount: Int,
                val category: String
            )
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AccountPagerViewHolder(
            LayoutAccountBinding.inflate(parent.context.inflater, parent, false)
        )

    override fun getItemCount(): Int = props.accountsInfo.size

    override fun onBindViewHolder(holder: AccountPagerViewHolder, position: Int) {
        holder.bind(props.accountsInfo[position])
    }

    class AccountPagerViewHolder(val binding: LayoutAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Props.AccountInfo) {
            val adapter =
                TransactionsRecyclerAdapter()
            binding.accountTitleTextView.text = item.accountTitle
            binding.accountTypeTextView.text = item.accountType
            binding.accountTransactionsRecycler.adapter = adapter
            binding.accountTransactionsRecycler.layoutManager =
                LinearLayoutManager(itemView.context)
            adapter.submitList(item.transactions)
        }
    }
}