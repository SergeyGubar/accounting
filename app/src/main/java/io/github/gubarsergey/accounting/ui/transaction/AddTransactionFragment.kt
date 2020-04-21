package io.github.gubarsergey.accounting.ui.transaction

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.data.account.Account
import io.github.gubarsergey.accounting.data.category.CategoryDto
import io.github.gubarsergey.accounting.databinding.FragmentAddTransactionBinding
import org.koin.android.ext.android.inject
import timber.log.Timber

class AddTransactionFragment : BaseFragment<FragmentAddTransactionBinding>() {

    private val interactor: AddTransactionsInteractor by inject()

    data class Props(
        val categories: List<CategoryDto>,
        val accounts: List<Account>
    )

    sealed class Events {
        object Success : Events()
        object Error : Events()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddTransactionBinding =
        FragmentAddTransactionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        interactor.props.observe(viewLifecycleOwner, Observer {
            val categoryAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it.categories.map { it.title })
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.addTransactionCategorySpinner.adapter = categoryAdapter

            val accountsAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it.accounts.map { it.title })
            accountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.addTransactionAccountsSpinner.adapter = accountsAdapter
        })

        interactor.events.observe(viewLifecycleOwner, Observer {
            when (it) {
                Events.Success -> {
                    findNavController().popBackStack()
                }
                Events.Error -> {
                    Timber.e("RIP")
                }
            }
        })
        interactor.loadCategories()
        interactor.loadAccounts()
    }

    private fun saveData() {
        this.interactor.addTransaction(
            binding.addTransactionAccountsSpinner.selectedItemPosition,
            binding.addTransactionAmount.text.toString().toInt(),
            binding.addTransactionCategorySpinner.selectedItemPosition,
            binding.addTransactionMessageEditText.text.toString()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_save -> {
                saveData()
                true
            }
            else -> {
                false
            }
        }
    }
}