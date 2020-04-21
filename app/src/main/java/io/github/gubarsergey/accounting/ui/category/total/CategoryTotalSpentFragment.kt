package io.github.gubarsergey.accounting.ui.category.total

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentCategoryTotalBinding
import io.github.gubarsergey.accounting.ui.Event
import io.github.gubarsergey.accounting.util.snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CategoryTotalSpentFragment : BaseFragment<FragmentCategoryTotalBinding>() {

    private val interactor: CategoryTotalSpentInteractor by viewModel()
    private val adapter: CategoryTotalSpentRecyclerAdapter = CategoryTotalSpentRecyclerAdapter()

    data class Props(
        val totalSpent: List<TotalSpent>
    ) {
        data class TotalSpent(
            val categoryId: String,
            val categoryName: String,
            val total: Double,
            val transactionsCount: Int
        )
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryTotalBinding =
        FragmentCategoryTotalBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.categoryTotalSpentRecycler.adapter = adapter
        binding.categoryTotalSpentRecycler.layoutManager = LinearLayoutManager(requireContext())
        interactor.props.observe(viewLifecycleOwner, Observer {
            render(it)
        })
        interactor.events.observe(viewLifecycleOwner, Observer {
            handleEvent(it)
        })
    }

    private fun render(props: Props) {
        Timber.d("render $props")
        adapter.submitList(props.totalSpent)
    }

    private fun handleEvent(event: Event<CategoryTotalSpentInteractor.CategoryTotalSpentEvent>) {
        Timber.d("handleEvent $event")
        event.ifNotHandled { e ->
            when (e) {
                is CategoryTotalSpentInteractor.CategoryTotalSpentEvent.LoadFailed -> {
                    snackbar(e.reason ?: "Something went wrong")
                }
            }
        }
    }
}