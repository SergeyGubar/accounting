package io.github.gubarsergey.accounting.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentAddCategoryBinding
import io.github.gubarsergey.accounting.util.snackbar
import org.koin.android.ext.android.inject

class AddCategoryFragment : BaseFragment<FragmentAddCategoryBinding>() {

    private val interactor: AddCategoryInteractor by inject()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddCategoryBinding = FragmentAddCategoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        interactor.events.observe(viewLifecycleOwner, Observer {
            it.ifNotHandled {
                when (it) {
                    AddCategoryInteractor.AddCategoryEvent.Success -> {
                        findNavController().popBackStack()
                    }
                    AddCategoryInteractor.AddCategoryEvent.Failure -> {
                        snackbar("Saving failed")
                    }
                }
            }
        })

        binding.addCategorySaveButton.setOnClickListener {
            trySave { title ->
                interactor.addCategory(title)
            }
        }
    }

    private fun trySave(onSuccess: (String) -> Unit) {
        val title = binding.addCategoryEditText.text.toString()
        if (title.isNotBlank()) {
            onSuccess(title)
        }
        snackbar("Input is not valid!")
    }
}