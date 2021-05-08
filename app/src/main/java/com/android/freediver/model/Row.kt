package com.android.freediver.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Row (

    @PrimaryKey(autoGenerate = true)
    val rowId : Long = 0L,

    @ColumnInfo
    var hold: Int = 0,

    @ColumnInfo
    var breath: Int = 0
)