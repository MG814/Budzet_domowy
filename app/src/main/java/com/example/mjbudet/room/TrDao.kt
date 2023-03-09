package com.example.mjbudet.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import java.time.LocalDate

@Dao
interface TrDao {
    @Query("SELECT * FROM tr_table ORDER BY SUBSTR(date,1,3) DESC,SUBSTR(date,4,6) DESC,SUBSTR(date,7,9) DESC")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT category, SUM(value) as sum FROM tr_table WHERE type = 'Wydatki' GROUP BY category")
    fun sumOutcome(): Flow<List<ValueCategory>>

    @Query("SELECT category, SUM(value) as sum FROM tr_table WHERE type = 'Przychody' GROUP BY category")
    fun sumIncome(): Flow<List<ValueCategory>>

    @Query("SELECT * FROM tr_table WHERE type LIKE 'Przychody' ORDER BY SUBSTR(date,1,3) DESC,SUBSTR(date,4,6) DESC,SUBSTR(date,7,9) DESC")
    fun getAllIncome(): Flow<List<Transaction>>

    @Query("SELECT * FROM tr_table WHERE type LIKE 'Wydatki' ORDER BY SUBSTR(date,1,3) DESC,SUBSTR(date,4,6) DESC,SUBSTR(date,7,9) DESC")
    fun getAllOutcome(): Flow<List<Transaction>>

    @Query("SELECT * FROM tr_table WHERE STRFTIME('%s', date) BETWEEN STRFTIME('%s', :date1) AND STRFTIME('%s',:date2)")
    fun getTransactionPeriod(date1: String, date2: String): Flow<List<Transaction>>

    @Query("SELECT category, SUM(value) as sum FROM tr_table WHERE " +
            "STRFTIME('%s', date) BETWEEN STRFTIME('%s', :date1) AND STRFTIME('%s',:date2) AND type = 'Wydatki' GROUP BY category")
    fun sumOutcomePeriod(date1: String, date2: String): Flow<List<ValueCategory>>

    @Insert
    fun insert(vararg transactions: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Update
    fun update(transaction: Transaction)
}