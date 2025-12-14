package com.yourname.spendsense.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income")
data class Income(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val source: String,
    val date: Long = System.currentTimeMillis(),
    val note: String = ""
)