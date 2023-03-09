package com.example.mjbudet.fragments.charts

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.mjbudet.MainActivity
import com.example.mjbudet.R
import com.example.mjbudet.databinding.FragmentIncomeBinding
import com.example.mjbudet.viewmodels.ViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF

class IncomeFragment : Fragment() {

    private var _binding: FragmentIncomeBinding ?= null
    private val binding get() = _binding!!
    private val mainVm by activityViewModels<ViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIncomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val entries: ArrayList<PieEntry> = ArrayList()
        val list: ArrayList<String> = ArrayList()

        (requireActivity() as MainActivity).setBottomNavVisibility(true)

        mainVm.sumIncome().observe(viewLifecycleOwner){listSum ->
            for(item in listSum){
                val pieEntry = PieEntry(item.sum.toFloat(),item.category)
                entries.add(pieEntry)
                list.add(item.category)
            }

            val dataSet = PieDataSet(entries,"")

            //Ustawienie lini oddzielajacych
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            val colors: ArrayList<Int> = ArrayList()
            colors.add(ContextCompat.getColor(requireContext(),R.color.green_40per))
            colors.add(ContextCompat.getColor(requireContext(),R.color.green_50per))
            colors.add(ContextCompat.getColor(requireContext(),R.color.green_60per))
            colors.add(ContextCompat.getColor(requireContext(),R.color.green_70per))

            dataSet.colors = colors

            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter(binding.incomeChart))
            data.setValueTextSize(15f)
            data.setValueTextColor(Color.WHITE)
            binding.incomeChart.data = data
        }

        binding.incomeChart.apply {
            centerText = "Przychody"
            setCenterTextSize(24f)
            setUsePercentValues(true)
            description.isEnabled = false

            setEntryLabelTextSize(15f)

            //Ustawienie wycięcia w środku
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)

            //Animacja wykresu
            animateY(1000, Easing.EaseInOutQuad)

            legend.isEnabled = false

            setExtraOffsets(5f, 10f, 5f, 5f)
            setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    binding.incomeChart.centerText = e?.y.toString() + " PLN"
                    binding.incomeChart.invalidate()
                }

                override fun onNothingSelected() {
                    binding.incomeChart.centerText = "Przychody"
                    binding.incomeChart.invalidate()
                }

            })
        }
    }

}