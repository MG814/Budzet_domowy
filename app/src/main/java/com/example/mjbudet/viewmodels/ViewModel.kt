package com.example.mjbudet.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mjbudet.room.AppTrDataBase
import com.example.mjbudet.room.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

open class ViewModel(app: Application): AndroidViewModel(app){
    private val repo = AppTrDataBase.getDataBase(app.applicationContext).trDao()
    private var selectedTransaction: Transaction? = null

    fun getAll() = repo.getAll().asLiveData(viewModelScope.coroutineContext)

    fun getAllIncomes() = repo.getAllIncome().asLiveData(viewModelScope.coroutineContext)

    fun getAllOutcomes() = repo.getAllOutcome().asLiveData(viewModelScope.coroutineContext)

    fun sumOutcome() = repo.sumOutcome().asLiveData(viewModelScope.coroutineContext)

    fun sumIncome() = repo.sumIncome().asLiveData(viewModelScope.coroutineContext)

    fun getTransactionPeriod(date1: String, date2: String) =
        repo.getTransactionPeriod(date1,date2).asLiveData(viewModelScope.coroutineContext)

    fun sumOutcomePeriod(date1: String, date2: String) =
        repo.sumOutcomePeriod(date1,date2).asLiveData(viewModelScope.coroutineContext)

    fun insert(transaction: Transaction) =
        CoroutineScope(Dispatchers.IO).launch {
            repo.insert(transaction)
        }

    fun update(transaction: Transaction) =
        CoroutineScope(Dispatchers.IO).launch {
            repo.update(transaction)
        }

    fun delete(transaction: Transaction) =
        CoroutineScope(Dispatchers.IO).launch {
            repo.delete(transaction)
        }

    fun selectTransaction(transaction: Transaction) {
        selectedTransaction = transaction
    }

    fun unselectTransaction(){
        selectedTransaction = null
    }

    fun getSelectedTransaction() = selectedTransaction
}


