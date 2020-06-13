package io.github.gubarsergey.accounting.ui.category.total

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentCategoryTotalBinding
import io.github.gubarsergey.accounting.ui.Event
import io.github.gubarsergey.accounting.util.snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.math.abs


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
//        binding.categoryTotalSpentRecycler.adapter = adapter
//        binding.categoryTotalSpentRecycler.layoutManager = LinearLayoutManager(requireContext())
        interactor.props.observe(viewLifecycleOwner, Observer {
            render(it)
        })
        interactor.events.observe(viewLifecycleOwner, Observer {
            handleEvent(it)
        })
    }

    private fun render(props: Props) = with(binding) {
        Timber.d("render $props")
//        adapter.submitList(props.totalSpent)

        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.centerText = "Categories"

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f

        chart.setDrawCenterText(true)

        chart.rotationAngle = 0f
        chart.isHighlightPerTapEnabled = true

        chart.animateY(1400, Easing.EaseInOutQuad)

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = -20f
        l.xOffset = 0f

        // entry label styling

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK)
        chart.setEntryLabelTextSize(14f)

        setData(props)
    }

    private fun setData(props: Props) {
        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        val negativeData = props.totalSpent.filter { it.total < 0 }.map { it.copy(total = abs(it.total)) }

        Timber.d("negativeData $negativeData")
        negativeData.forEach {
            entries.add(
                PieEntry(
                    it.total.toFloat(),
                    it.categoryName
                )
            )
        }
        val dataSet = PieDataSet(entries, "Categories Legend")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(binding.chart))
        data.setValueTextSize(14f)
        data.setValueTextColor(Color.BLACK)
        binding.chart.setData(data)

        // undo all highlights
        binding.chart.highlightValues(null)
        binding.chart.invalidate()
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