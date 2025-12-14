package com.yourname.spendsense.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val category: String,
    val date: Long = System.currentTimeMillis(),
    val note: String = "",
    val paymentMethod: String = "Cash"
)