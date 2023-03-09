package com.example.mjbudet.fragments.charts


import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mjbudet.MainActivity
import com.example.mjbudet.MyAdapter
import com.example.mjbudet.R
import com.example.mjbudet.databinding.FragmentOutcomeBinding
import com.example.mjbudet.viewmodels.SummaryFrViewModel
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
import java.util.*
import kotlin.collections.ArrayList


class OutcomeFragment : Fragment(),DatePickerDialog.OnDateSetListener {
    private var _binding: FragmentOutcomeBinding?= null
    private val binding get() = _binding!!
    private val mainVm by activityViewModels<ViewModel>()
    private val summaryVm by activityViewModels<SummaryFrViewModel>()
    private val calendar = Calendar.getInstance()
    private var x = true
    private var from = ""
    private var to  = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOutcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var correctMonth = (month + 1).toString()
        var day = dayOfMonth.toString()

        if(dayOfMonth < 10)
            day = "0$day"
        if(month + 1 < 10)
            correctMonth = "0$correctMonth"

        if(x){
            binding.editTextDate1.setText("$year-$correctMonth-$day")

        }

        if(!x){
            binding.editTextDate2.setText("$year-$correctMonth-$day")

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val entries: ArrayList<PieEntry> = ArrayList()
        val list: ArrayList<String> = ArrayList()

        (requireActivity() as MainActivity).setBottomNavVisibility(true)

        binding.editTextDate1.setOnClickListener{
            DatePickerDialog(requireContext(),R.style.my_dialog_theme, this, calendar.get(Calendar.YEAR),calendar.get(
                Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            x = true
        }

        binding.editTextDate2.setOnClickListener{
            DatePickerDialog(requireContext(),R.style.my_dialog_theme, this, calendar.get(Calendar.YEAR),calendar.get(
                Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            x = false
        }

        binding.button2.setOnClickListener {
            (requireActivity() as MainActivity).setBottomNavVisibility(true)

            from = binding.editTextDate1.text.toString()
            to = binding.editTextDate2.text.toString()

            mainVm.sumOutcomePeriod(from,to).observe(viewLifecycleOwner){ transactions ->
                for(item in transactions){
                    val pieEntry = PieEntry(item.sum.toFloat(), item.category)
                    entries.add(pieEntry)
                    list.add(item.category)
                }
            }

            if(entries.isNotEmpty()){
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
                data.setValueFormatter(PercentFormatter(binding.outcomeChart))
                data.setValueTextSize(15f)
                data.setValueTextColor(Color.WHITE)
                binding.outcomeChart.data = data

                binding.outcomeChart.apply {
                    centerText = "Wydatki"
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
                    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            binding.outcomeChart.centerText = "-" + e?.y.toString() + " PLN"
                            binding.outcomeChart.invalidate()
                        }

                        override fun onNothingSelected() {
                            binding.outcomeChart.centerText = "Wydatki"
                            binding.outcomeChart.invalidate()
                        }

                    })
                }
            }
        }


        }
}