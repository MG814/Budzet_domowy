package com.example.mjbudet.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class], version = 1)
abstract class AppTrDataBase : RoomDatabase() {
    abstract fun trDao(): TrDao

    companion object{
        @Volatile
        private var INSTANCE: AppTrDataBase? = null

        fun getDataBase(context: Context): AppTrDataBase{
            val tempInstance = INSTANCE

            if(tempInstance!=null)
                return tempInstance
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppTrDataBase::class.java,
                    "app_Tr_Data_Base",
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}