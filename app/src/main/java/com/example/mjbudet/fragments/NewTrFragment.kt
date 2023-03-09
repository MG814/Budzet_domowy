package com.example.mjbudet.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.mjbudet.MainActivity
import com.example.mjbudet.R
import com.example.mjbudet.databinding.FragmentNewTrBinding
import com.example.mjbudet.room.Transaction
import com.example.mjbudet.viewmodels.ViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NewTrFragment : Fragment(R.layout.fragment_new_tr), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentNewTrBinding ?= null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private val mainVm by activityViewModels<ViewModel>()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewTrBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var correctMonth = (month + 1).toString()
        var day = dayOfMonth.toString()

        if(dayOfMonth < 10)
            day = "0$day"
        if(month + 1 < 10)
            correctMonth = "0$correctMonth"

        binding.editTextDate.setText("$year-$correctMonth-$day")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val types = resources.getStringArray(R.array.type)
        val categories = resources.getStringArray(R.array.category)
        var arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, types)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        binding.autoCompleteTextViewCat.setAdapter(arrayAdapter)

        binding.editTextDate.setOnClickListener{
            DatePickerDialog(requireContext(),R.style.my_dialog_theme, this, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.saveBtn.setOnClickListener {
            if(binding.editTextValue.text?.isNotEmpty() == true &&
                binding.autoCompleteTextView.text?.isNotEmpty() == true &&
                binding.autoCompleteTextViewCat.text?.isNotEmpty() == true){
                val category = binding.autoCompleteTextViewCat.text.toString()
                val type = binding.autoCompleteTextView.text.toString()
                val value = binding.editTextValue.text.toString().toDouble()
                val date = binding.editTextDate.text.toString()
                val dsc = binding.editTextDesc.text.toString()

                val tr = Transaction(null, category, type, value, date, dsc)
                mainVm.insert(tr)
                requireActivity().onBackPressed()
            }else
                Toast.makeText(requireContext(),"Wprowadź kwotę, kategorię oraz typ transakcji",Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.editTextDate.setText(LocalDateTime.now().format(formatter))
    }

    override fun onStart() {
        super.onStart()
        if(!NewTrFragment().isDetached)
            (requireActivity() as MainActivity).setBottomNavVisibility(false)
        clearAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun clearAll(){
        binding.autoCompleteTextViewCat.text?.clear()
        binding.autoCompleteTextView.text.clear()
        binding.editTextValue.text?.clear()
        binding.editTextDate.text?.clear()
        binding.editTextDesc.text?.clear()
    }

}