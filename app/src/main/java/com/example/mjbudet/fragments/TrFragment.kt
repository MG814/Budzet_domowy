package com.example.mjbudet.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mjbudet.MainActivity
import com.example.mjbudet.MyAdapter
import com.example.mjbudet.R
import com.example.mjbudet.viewmodels.ViewModel
import com.example.mjbudet.databinding.FragmentTrBinding

class TrFragment : Fragment(R.layout.fragment_tr) {

    private var _binding: FragmentTrBinding ?= null
    private val binding get() = _binding!!
    private val mainVm by activityViewModels<ViewModel>()
    private var type = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).setBottomNavVisibility(true)

        sortTypeTransaction(type)

        binding.autoCompleteTextViewMain.setOnDismissListener {
            type = binding.autoCompleteTextViewMain.text.toString()
            sortTypeTransaction(type)
        }
    }

    override fun onStart() {
        super.onStart()
        // calling the action bar
        val actionBar = (requireActivity() as MainActivity).supportActionBar
        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        val sortItems = resources.getStringArray(R.array.sort)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, sortItems)
        binding.autoCompleteTextViewMain.setAdapter(arrayAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sortTypeTransaction(type: String){
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        val x = if(type == "Przychody")
            mainVm.getAllIncomes()
        else if(type == "Wydatki")
            mainVm.getAllOutcomes()
        else
            mainVm.getAll()

        x.observe(viewLifecycleOwner){ transactions ->
            binding.recyclerView.adapter = MyAdapter(transactions){ transaction, position ->
                mainVm.selectTransaction(transaction)
                val action = TrFragmentDirections.actionTrFragmentToEditFragment()
                findNavController().navigate(action)
            }
        }
    }
}