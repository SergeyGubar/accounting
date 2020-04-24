package io.github.gubarsergey.accounting.ui.transaction.report

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentTimeRangeReportBinding
import io.github.gubarsergey.accounting.util.snackbar
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

class TimeRangeReportFragment : BaseFragment<FragmentTimeRangeReportBinding>() {

    data class Props(
        val startTime: String?,
        val endTime: String?,
        val accountsInfo: List<Pair<String, String>>,
        val dailySpent: List<DailySpent>
    ) {
        data class DailySpent(
            val createdAt: String,
            val totalSpent: Double,
            val numberOfTransactions: Int
        )
    }

    private val interactor: TimeRangeReportInteractor by inject()

    private val categoryAdapter by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf()
        )
    }
    private val dailySpentAdapter = TimeRangeReportAdapter()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTimeRangeReportBinding =
        FragmentTimeRangeReportBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reportTimeRangeStartTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val displayYear = calendar.get(Calendar.YEAR)
            val displayMonth = calendar.get(Calendar.MONTH)
            val displayDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                    interactor.startTimeSelected(selectedDate.time)
                },
                displayYear,
                displayMonth,
                displayDayOfMonth
            ).show()
        }

        binding.reportEndTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val displayYear = calendar.get(Calendar.YEAR)
            val displayMonth = calendar.get(Calendar.MONTH)
            val displayDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    interactor.endTimeSelected(selectedDate.time)
                },
                displayYear,
                displayMonth,
                displayDayOfMonth
            ).show()
        }

        binding.reportTimeRangeGenerateButton.setOnClickListener {
            interactor.generateReport()
        }

        interactor.props.observe(viewLifecycleOwner, Observer {
            render(it)
        })

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.reportAccountSpinner.adapter = categoryAdapter
        binding.reportAccountSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    interactor.selectAccount(position)
                }

            }

        binding.reportResultsRecycler.adapter = dailySpentAdapter
        binding.reportResultsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.reportResultsRecycler.isNestedScrollingEnabled = false

        interactor.events.observe(viewLifecycleOwner, Observer {
            it.ifNotHandled { e ->
                when (e) {
                    TimeRangeReportInteractor.TimeRangeReportEvent.InputNotValid -> {
                        snackbar("Input not valid")
                    }
                    TimeRangeReportInteractor.TimeRangeReportEvent.NetworkError -> {
                        snackbar("Network request failed")
                    }
                }
            }
        })
    }

    private fun render(props: Props) {
        Timber.d("render $props")
        if (props.startTime != null) {
            binding.reportTimeStartTextView.text = props.startTime
        }
        if (props.endTime != null) {
            binding.reportTimeEndTextView.text = props.endTime
        }
        if (props.accountsInfo.isNotEmpty()) {
            if (categoryAdapter.isEmpty) {
                categoryAdapter.addAll(props.accountsInfo.map { it.second })
            }
        }
        if (props.dailySpent.isNotEmpty()) {
            dailySpentAdapter.submitList(props.dailySpent)
        }
    }
}