package com.example.mjbudet.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.sql.Date

class SummaryFrViewModel(app: Application): AndroidViewModel(app) {
    private var from = ""
    private var to = ""

    fun setFrom(s: String){
        from = s
    }

    fun getFrom(): String {
        return from
    }

    fun setTo(s: String){
        to = s
    }

    fun getTo(): String {
        return to
    }

}