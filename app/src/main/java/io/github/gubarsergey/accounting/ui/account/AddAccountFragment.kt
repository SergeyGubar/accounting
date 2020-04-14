package io.github.gubarsergey.accounting.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentAddAccountBinding
import org.koin.android.ext.android.inject

class AddAccountFragment : BaseFragment<FragmentAddAccountBinding>() {

    private val addAccountInteractor: AddAccountInteractor by inject()
    private val accountTypes = arrayOf(
        "Card",
        "Cash"
    )

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddAccountBinding = FragmentAddAccountBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addAccountTypePicker.displayedValues = accountTypes
        binding.addAccountTypePicker.minValue = 0
        binding.addAccountTypePicker.maxValue = 1

        addAccountInteractor.events.observe(viewLifecycleOwner, Observer {
            it.ifNotHandled {
                when (it) {
                    AddAccountInteractor.AddAccountEvent.AddAccountSuccess -> {
                        findNavController().popBackStack()
                    }
                    AddAccountInteractor.AddAccountEvent.AddAccountFailure -> {
                        Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })

        binding.addAccountSaveButton.setOnClickListener {
            validate(
                binding.addAccountTitleEditText.text.toString(),
                binding.addAccountCurrencyEditText.text.toString(),
                accountTypes[binding.addAccountTypePicker.value],
                binding.addAccountAmountEditText.text.toString()
            ) { title, currency, type, amount ->
                addAccountInteractor.addAccount(title, currency, type, amount)
            }
        }
    }

    private fun validate(
        title: String,
        currency: String,
        type: String,
        amount: String,
        onSuccess: (String, String, String, Int) -> Unit
    ) {
        if (title.isNotBlank() && currency.isNotBlank() && type.isNotBlank() && amount.isNotBlank()) {
            onSuccess(title, currency, type, amount.toInt())
            return
        }
        Snackbar.make(binding.root, "Input is not valid", Snackbar.LENGTH_SHORT)
            .show()
    }
}