package com.android.freediver.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.freediver.model.BestTime

@Database(entities = [BestTime::class], version = 1, exportSchema = false)
abstract class FreediverDatabase : RoomDatabase() {

    abstract val bestTimeDao: BestTimeDao

    companion object {
        @Volatile
        private var INSTANCE : FreediverDatabase? = null

        fun getInstance(context: Context): FreediverDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FreediverDatabase::class.java,
                        "FreediverDataBase"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

