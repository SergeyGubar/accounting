package io.github.gubarsergey.accounting.ui.remaining

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentChangeRemainingBinding
import io.github.gubarsergey.accounting.util.snackbar
import org.koin.android.ext.android.inject
import timber.log.Timber

class ChangeRemainingFragment : BaseFragment<FragmentChangeRemainingBinding>() {

    data class Props(
        val selectedAccount: String?,
        val accounts: List<Pair<String, String>>
    )

    private val interactor: ChangeRemainingInteractor by inject()

    private val accountsAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf<String>()
        )
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChangeRemainingBinding =
        FragmentChangeRemainingBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        interactor.submit(ChangeRemainingIntent.ViewLoaded)
        interactor.props.observe(viewLifecycleOwner, Observer {
            render(it)
        })
        interactor.events.observe(viewLifecycleOwner, Observer { ew ->
            ew.ifNotHandled {
                handle(it)
            }
        })

        accountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.accountsSpinner.adapter = accountsAdapter
        binding.saveButton.setOnClickListener {
            interactor.submit(
                ChangeRemainingIntent.ChangeRemaining(
                    binding.accountsSpinner.selectedItemPosition,
                    binding.amountEditText.text.toString()
                )
            )
        }
    }

    private fun render(props: Props) {
        Timber.d("render $props")
        accountsAdapter.clear()
        accountsAdapter.addAll(props.accounts.map { it.second })
    }

    private fun handle(event: ChangeRemainingEvent) {
        when (event) {
            ChangeRemainingEvent.LoadFailed, ChangeRemainingEvent.SaveFailed -> snackbar("Loading accounts failed")
            ChangeRemainingEvent.ValidationFailed -> snackbar("Validation failed")
            ChangeRemainingEvent.SaveSuccess -> findNavController().popBackStack()
        }
    }
}