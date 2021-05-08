package com.android.freediver.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CO2Table (

    @PrimaryKey(autoGenerate = true)
    val co2TableId : Long = 0L,

    @ColumnInfo
    var name: String = "",

    @ColumnInfo
    var rows: List<Row> = ArrayList(),

    @ColumnInfo
    var date: Long = 0L
)