package io.github.gubarsergey.accounting.ui.prediction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.databinding.FragmentPredictionsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.math.absoluteValue


class PredictionFragment : BaseFragment<FragmentPredictionsBinding>() {

    data class Props(
        val accountsNames: List<String>,
        val predictions: List<Prediction>,
        val formula: String,
        val coef: String,
        val points: String
    ) {
        data class Prediction(
            val month: String,
            val prediction: Double
        )
    }

    private val interactor: PredictionsInteractor by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPredictionsBinding = FragmentPredictionsBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        interactor.props.observe(viewLifecycleOwner, Observer {
            render(it)
        })
        interactor.submit(PredictionsIntent.LoadAccounts)
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                interactor.submit(PredictionsIntent.SpinnerItemClicked(position))
            }
        }
    }

    private fun render(props: Props) {

        if (binding.spinner.adapter == null) {

            val categoryAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                props.accountsNames
            )
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = categoryAdapter
        }

        if (props.predictions.isNotEmpty()) {

            binding.formulaTextView.text = getString(R.string.formula_n, props.formula)
            binding.coefficientTextView.text = getString(R.string.formula_n, props.coef)
            binding.pointsTextView.text = props.points
            with(binding) {

                chart.setDrawBarShadow(false)

                chart.setDrawValueAboveBar(true)

                chart.getDescription().setEnabled(false)

                // if more than 60 entries are displayed in the chart, no values will be
                // drawn

                // if more than 60 entries are displayed in the chart, no values will be
                // drawn
                chart.setMaxVisibleValueCount(60)

                // scaling can now only be done on x- and y-axis separately

                // scaling can now only be done on x- and y-axis separately
                chart.setPinchZoom(false)

                // draw shadows for each bar that show the maximum value
                // chart.setDrawBarShadow(true);


                // draw shadows for each bar that show the maximum value
                // chart.setDrawBarShadow(true);
                chart.setDrawGridBackground(false)

                val xl: XAxis = chart.getXAxis()
                xl.position = XAxisPosition.BOTTOM
                xl.setDrawAxisLine(true)
                xl.setDrawGridLines(false)
                xl.granularity = 10f

                val yl: YAxis = chart.getAxisLeft()
                yl.setDrawAxisLine(true)
                yl.setDrawGridLines(true)
                yl.axisMinimum = 0f // this replaces setStartAtZero(true)
                yl.axisMaximum = 10000f

//        yl.setInverted(true);

                //        yl.setInverted(true);
                val yr: YAxis = chart.getAxisRight()
                yr.setDrawAxisLine(true)
                yr.setDrawGridLines(false)
                yr.axisMinimum = 0f // this replaces setStartAtZero(true)

//        yr.setInverted(true);

                //        yr.setInverted(true);
                chart.setFitBars(true)
                chart.animateY(2000)

                // setting data

                // setting data


                val l: Legend = chart.getLegend()
                l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                l.orientation = Legend.LegendOrientation.HORIZONTAL
                l.setDrawInside(false)
                l.formSize = 8f
                l.xEntrySpace = 4f
                l.yOffset = -40f
                setData(props)
            }
        }
    }

    private fun setData(props: Props) {
        Timber.d("set data $props")

        val reversed = props.predictions.reversed()

        with(binding) {

            val barWidth = 9f
            val spaceForBar = 10f
            val values: ArrayList<BarEntry> = ArrayList()

            for (i in 0 until reversed.size) {
                val item = reversed[i]
                values.add(
                    BarEntry(
                        i * spaceForBar, item.prediction.toFloat().absoluteValue,
                        item.month
                    )
                )
            }

            val set1: BarDataSet

            if (chart.data != null &&
                chart.data.dataSetCount > 0
            ) {
                set1 = chart.data.getDataSetByIndex(0) as BarDataSet
                set1.values = values
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged()
            } else {

                set1 = BarDataSet(values, "Predictions for left months")
                set1.setDrawIcons(false)
                val dataSets: ArrayList<IBarDataSet> = ArrayList()
                dataSets.add(set1)
                val data = BarData(dataSets)
                data.setValueFormatter(object : ValueFormatter() {
                    override fun getBarLabel(barEntry: BarEntry?): String {
                        return barEntry?.data.toString()
                    }
                })
                data.setValueTextSize(10f)
                data.barWidth = barWidth
                chart.data = data
            }
        }

    }
}