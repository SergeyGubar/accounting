package io.github.gubarsergey.accounting.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentMainBinding
import org.koin.android.ext.android.inject

class AccountsFragment : BaseFragment<AccountsFragment.Props, FragmentMainBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)

    data class Props(
        val accountTitle: String = "",
        val accountType: String = "",
        val transactions: List<Transaction> = emptyList()
    ) {
        data class Transaction(
            val id: String,
            val amount: Int,
            val category: String
        )
    }

    private val adapter = AccountanceRecyclerAdapter()
    private val interactor: AccountsInteractor by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.accountsTransactionsRecycler.adapter = adapter
        binding.accountsTransactionsRecycler.layoutManager = LinearLayoutManager(requireContext())
        interactor.props.observe(viewLifecycleOwner, Observer { props ->
            render(props)
        })
        interactor.loadAccounts()
    }

    private fun render(props: Props) {
        binding.accountsTitleTextView.text = props.accountTitle
        binding.accountsTypeTextView.text = props.accountType
        adapter.submitList(props.transactions)
    }
}
