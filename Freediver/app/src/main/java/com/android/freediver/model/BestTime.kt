package com.android.freediver.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BestTime (

    @PrimaryKey(autoGenerate = true)
    val bestTimeId : Long = 0L,

    @ColumnInfo
    var duration: Long,

    @ColumnInfo
    var contractionsStart: Long,

    @ColumnInfo
    var pulseData : Long,

    @ColumnInfo
    var notes : String,

    @ColumnInfo
    var date: Long
)