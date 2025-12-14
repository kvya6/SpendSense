package com.yourname.spendsense.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val month: String,
    val limitAmount: Double,
    val category: String = "Overall"
)