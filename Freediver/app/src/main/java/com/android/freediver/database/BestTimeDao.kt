package com.android.freediver.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.freediver.model.BestTime

@Dao
interface BestTimeDao {

    @Insert
    suspend fun insert(bestTime: BestTime)

    @Update
    suspend fun update(bestTime: BestTime)

    @Delete
    suspend fun delete(bestTime: BestTime)

    @Query("SELECT * FROM BestTime WHERE bestTimeId = :key")
    suspend fun get (key: Long): BestTime

    @Query("DELETE FROM BestTime")
    suspend fun clear()

    /*@Query("SELECT * FROM BestTime ORDER BY bestTimeId DESC")
    suspend fun getAll() : LiveData<List<BestTime>>*/

    @Query("SELECT * FROM BestTime ORDER BY duration DESC LIMIT 1")
    suspend fun getCurrentBestTime() : BestTime?
}
