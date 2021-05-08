package com.android.freediver

import android.util.Log
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.android.freediver.database.BestTimeDao
import com.android.freediver.database.FreediverDatabase
import com.android.freediver.model.BestTime
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class FreediverDatabaseTest {

    private lateinit var bestTimeDao: BestTimeDao
    private lateinit var database: FreediverDatabase

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        database = Room.inMemoryDatabaseBuilder(context, FreediverDatabase::class.java)
            .build()
        bestTimeDao = database.bestTimeDao
    }

    @After
    fun closeDataBase() {
        database.close()
    }

    @Test
    fun insertAndGetBestTime() = runBlocking{
        val newBestTime = BestTime(10,1,1,1,"Test", System.currentTimeMillis())
        bestTimeDao.insert(newBestTime)
        val theBestTime = bestTimeDao.get(newBestTime.bestTimeId)
        assertEquals(newBestTime, theBestTime)
    }

    @Test
    fun getCurrentBestTime() = runBlocking{
        val time1 = BestTime(1,1,1,1,"Test1", System.currentTimeMillis())
        val time2 = BestTime(2,150,9,100,"Test2", System.currentTimeMillis())
        val time3 = BestTime(3,90,4,10000,"Test3", System.currentTimeMillis())
        bestTimeDao.insert(time1)
        bestTimeDao.insert(time2)
        bestTimeDao.insert(time3)
        val currentBestTime = bestTimeDao.getCurrentBestTime()
        assertEquals(time2, currentBestTime)
    }
}