package com.example.mjbudet.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.DatePicker
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mjbudet.MainActivity
import com.example.mjbudet.MyAdapter
import com.example.mjbudet.R
import com.example.mjbudet.databinding.FragmentSummaryBinding
import com.example.mjbudet.viewmodels.SummaryFrViewModel
import com.example.mjbudet.viewmodels.ViewModel
import java.util.*

class SummaryFragment : Fragment(),DatePickerDialog.OnDateSetListener {
    private var _binding: FragmentSummaryBinding?= null
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
    ): View {
        _binding = FragmentSummaryBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
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

        binding.showBtn.setOnClickListener {
            (requireActivity() as MainActivity).setBottomNavVisibility(true)
            binding.recyclerViewPeriod.layoutManager = GridLayoutManager(requireContext(), 1)

            from = binding.editTextDate1.text.toString()
            to = binding.editTextDate2.text.toString()

            summaryVm.setFrom(from)
            summaryVm.setTo(to)

            mainVm.getTransactionPeriod(from,to).observe(viewLifecycleOwner){ transactions ->
                binding.recyclerViewPeriod.adapter = MyAdapter(transactions){ transaction, position ->
                    mainVm.selectTransaction(transaction)
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.chart)
        item.isVisible = true
    }

    override fun onStart() {
        super.onStart()
        // calling the action bar
        val actionBar = (requireActivity() as MainActivity).supportActionBar
        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}