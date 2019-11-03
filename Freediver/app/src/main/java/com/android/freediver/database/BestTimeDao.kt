package com.android.freediver.database

import androidx.room.*
import com.android.freediver.model.BestTime

@Dao
interface BestTimeDao {

    @Insert
    fun insert(bestTime: BestTime)

    @Update
    fun update(bestTime: BestTime)

    @Delete
    fun delete(bestTime: BestTime)

    @Query("SELECT * FROM BestTime WHERE bestTimeId = :key")
    fun get (key: Long): BestTime

    @Query("DELETE FROM BestTime")
    fun clear()

    @Query("SELECT * FROM BestTime ORDER BY bestTimeId DESC")
    fun getAll()

    @Query("SELECT * FROM BestTime ORDER BY duration DESC LIMIT 1")
    fun getCurrentBestTime()
}
