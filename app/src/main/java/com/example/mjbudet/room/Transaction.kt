package com.example.mjbudet.room

import android.app.DatePickerDialog
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "tr_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    val category: String,
    val type: String,
    val value: Double,
    val date: String,
    @ColumnInfo(name = "description") val desc: String
)
