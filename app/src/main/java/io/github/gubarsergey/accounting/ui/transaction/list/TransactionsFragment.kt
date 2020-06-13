package io.github.gubarsergey.accounting.ui.transaction.list

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.databinding.FragmentTransactionsBinding
import io.github.gubarsergey.accounting.util.materialAlertDialog
import org.koin.android.ext.android.inject
import timber.log.Timber

class TransactionsFragment : BaseFragment<FragmentTransactionsBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTransactionsBinding = FragmentTransactionsBinding.inflate(inflater, container, false)

    data class Props(
        val accountsInfo: List<AccountInfo> = emptyList()
    ) {
        data class AccountInfo(
            val id: String,
            val title: String,
            val type: String,
            val amount: Double,
            val transactions: List<Transaction>
        )

        data class Transaction(
            val id: String,
            val amount: Int,
            val category: String,
            val message: String?,
            val date: String
        )
    }

    private val adapter = TransactionsPagerAdapter(::onDeleteClicked)
    private val interactor: AccountsInteractor by inject()
    private var props = Props()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        interactor.props.observe(viewLifecycleOwner, Observer { props ->
            render(props)
        })
        binding.accountsAddFab.setOnClickListener {
            findNavController().navigate(TransactionsFragmentDirections.actionMainFragmentToAddTransactionFragment())
        }
        binding.accountsViewPager.adapter = adapter
        binding.accountsViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Kostili moi kostili
                Handler().postDelayed({
                    interactor.loadTransactions(adapter.props.accountsInfo[position].id)
                }, 1000)
            }
        })
        TabLayoutMediator(binding.accountsTabLayout, binding.accountsViewPager) { tab, position ->
            tab.text = this.props.accountsInfo[position].title
        }.attach()
        interactor.loadAccounts()
    }

    private fun render(props: Props) {
        Timber.d("render $props")
        this.props = props
        val pagerProps = TransactionsPagerAdapter.Props(
            props.accountsInfo.map {
                TransactionsPagerAdapter.Props.AccountInfo(
                    it.id,
                    it.title,
                    it.type,
                    it.amount,
                    it.transactions.map { transaction ->
                        TransactionsPagerAdapter.Props.AccountInfo.Transaction(
                            transaction.id,
                            transaction.amount,
                            transaction.category,
                            transaction.message ?: "",
                            transaction.date
                        )
                    })
            }
        )
        adapter.props = pagerProps
    }

    private fun onDeleteClicked(id: String, name: String) {
        materialAlertDialog(
            "Delete $name?", "This action will delete selected transaction.", getString(
                R.string.ok
            ), getString(R.string.cancel), {
                interactor.deleteTransaction(id)
            }
        )
    }
}
