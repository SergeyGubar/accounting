package io.github.gubarsergey.accounting.ui.category.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentCategoryListBinding
import org.koin.android.ext.android.inject
import timber.log.Timber

class CategoryListFragment : BaseFragment<FragmentCategoryListBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryListBinding = FragmentCategoryListBinding.inflate(inflater, container, false)

    private val adapter =
        CategoryListAdapter()
    private val categoryListInteractor: CategoryListInteractor by inject()

    data class Props(
        val categories: List<Category>
    ) {
        data class Category(
            val id: String,
            val title: String
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.categoryListRecycler.adapter = adapter
        binding.categoryListRecycler.layoutManager = LinearLayoutManager(requireContext())
        categoryListInteractor.props.observe(viewLifecycleOwner, Observer {
            render(it)
        })
    }

    private fun render(props: Props) {
        Timber.d("render $props")
        adapter.submitList(props.categories)
    }
}