package com.lukasbrand.sharedwallet.data

import android.location.Location
import java.util.*

data class Expense(
    val id: Long,
    val name: String,
    val owner: User,
    val participants: List<User>,
    val location: Location,
    val creationDate: Date,
    val dueDate: Date,
    val individualExpense: List<Int>,
    val isPaid: List<Boolean>,
    //val isArchived: Boolean
)
