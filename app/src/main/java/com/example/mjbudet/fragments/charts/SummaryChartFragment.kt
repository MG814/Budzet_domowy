package com.example.mjbudet.fragments.charts

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.mjbudet.MainActivity
import com.example.mjbudet.R
import com.example.mjbudet.databinding.FragmentSummaryChartBinding
import com.example.mjbudet.viewmodels.SummaryFrViewModel
import com.example.mjbudet.viewmodels.ViewModel
import com.github.mikephil.charting.data.*

class SummaryChartFragment : Fragment() {
    private var _binding: FragmentSummaryChartBinding ?= null
    private val binding get() = _binding!!
    private val mainVm by activityViewModels<ViewModel>()
    private val summaryVm by activityViewModels<SummaryFrViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSummaryChartBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lineentry = ArrayList<Entry>()
        val lineentry2 = ArrayList<Entry>()
//        val dateTr = ArrayList<String>()

        (requireActivity() as MainActivity).setBottomNavVisibility(false)

        binding.fromTV.text = "Od: " + summaryVm.getFrom()
        binding.toTV.text = "Do: " + summaryVm.getTo()
        var x: Float
        var y: Float


        mainVm.getTransactionPeriod(summaryVm.getFrom(),summaryVm.getTo()).observe(viewLifecycleOwner) { transactions ->
            for (i in transactions.indices) {
                x = i.toFloat()
                y = transactions[i].value.toFloat()
                if(transactions[i].type == "Przychody")
                    lineentry.add(Entry(x, y))
                else
                    lineentry2.add(Entry(x, y))
            }

//            binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter()

            val lineDataSet = LineDataSet(lineentry, "Przychody")

            lineDataSet.color = ContextCompat.getColor(requireContext(), R.color.purple_700)
            lineDataSet.circleRadius = 0f
            lineDataSet.setDrawFilled(true)
            lineDataSet.fillColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
            lineDataSet.fillAlpha = 30

            val lineDataSet2 = LineDataSet(lineentry2, "Wydatki")

            lineDataSet2.color = ContextCompat.getColor(requireContext(), R.color.red_70per)
            lineDataSet2.circleRadius = 0f
            lineDataSet2.setDrawFilled(true)
            lineDataSet2.fillColor = ContextCompat.getColor(requireContext(), R.color.red_200)
            lineDataSet2.fillAlpha = 30

            val lineData: LineData

            if(lineentry.size > 1 && lineentry2.size > 1)
                lineData = LineData(lineDataSet, lineDataSet2)
            else if(lineentry.size > 1 && lineentry2.size <= 1)
                lineData = LineData(lineDataSet)
            else if(lineentry.size <= 1 && lineentry2.size > 1)
                lineData = LineData(lineDataSet2)
            else
                lineData = LineData()

            lineData.setValueTextSize(15f)

            binding.lineChart.animateXY(1000, 1000)

            binding.lineChart.data = lineData
            binding.lineChart.invalidate()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.add)
        item.isVisible = false
    }

}


