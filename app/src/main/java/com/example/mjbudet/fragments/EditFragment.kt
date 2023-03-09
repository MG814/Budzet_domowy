package com.example.mjbudet.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.activityViewModels
import com.example.mjbudet.MainActivity
import com.example.mjbudet.R
import com.example.mjbudet.databinding.FragmentEditBinding
import com.example.mjbudet.room.Transaction
import com.example.mjbudet.viewmodels.ViewModel
import java.util.*

class EditFragment : Fragment(R.layout.fragment_edit), DatePickerDialog.OnDateSetListener{

    private var _binding: FragmentEditBinding ?= null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private val mainVm by activityViewModels<ViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(layoutInflater, container, false)
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

        setDataTransaction(mainVm.getSelectedTransaction()!!)

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
            val category = binding.autoCompleteTextViewCat.text.toString()
            val type = binding.autoCompleteTextView.text.toString()
            val value = binding.editTextValue.text.toString().toDouble()
            val date = binding.editTextDate.text.toString()
            val dsc = binding.editTextDesc.text.toString()

            val tr = Transaction(mainVm.getSelectedTransaction()!!.uid, category, type, value, date, dsc)
            mainVm.update(tr)
            requireActivity().onBackPressed()
        }

        binding.deleteBtn.setOnClickListener {
            mainVm.delete(mainVm.getSelectedTransaction()!!)
            mainVm.unselectTransaction()
            requireActivity().onBackPressed()
        }

    }

    override fun onStart() {
        super.onStart()
        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        if(!EditFragment().isDetached)
            (requireActivity() as MainActivity).setBottomNavVisibility(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setDataTransaction(transaction: Transaction){
        setCategory(transaction.category)
        setType(transaction.type)
        setValue(transaction.value.toString())
        setDate(transaction.date)
        setDescription(transaction.desc)
    }

    private fun setDescription(desc: String) {
        binding.editTextDesc.setText(desc)
    }

    private fun setDate(date: String) {
        binding.editTextDate.setText(date)
    }

    private fun setValue(value: String) {
        binding.editTextValue.setText(value)
    }

    private fun setType(type: String) {
        binding.autoCompleteTextView.setText(type)
    }

    private fun setCategory(category: String){
        binding.autoCompleteTextViewCat.setText(category)
    }

}