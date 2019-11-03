package com.android.freediver

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.android.freediver.database.BestTimeDao
import com.android.freediver.database.FreediverDatabase
import com.android.freediver.model.BestTime
import junit.framework.Assert.assertEquals
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
            .allowMainThreadQueries()
            .build()
        bestTimeDao = database.bestTimeDao
    }

    @After
    fun closeDataBase() {
        database.close()
    }

    @Test
    fun insertAndGetBestTime() {
        val newBestTime = BestTime(1,1,1,1,"Test", System.currentTimeMillis())
        bestTimeDao.insert(newBestTime)
        val theBestTime = bestTimeDao.get(newBestTime.bestTimeId)
        assertEquals(newBestTime, theBestTime)
    }
}