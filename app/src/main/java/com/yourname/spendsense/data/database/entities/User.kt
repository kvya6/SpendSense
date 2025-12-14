package com.yourname.spendsense.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val currency: String = "INR",
    val monthlyIncome: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)