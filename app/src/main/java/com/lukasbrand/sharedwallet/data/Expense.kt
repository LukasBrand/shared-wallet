package com.lukasbrand.sharedwallet.data

import android.location.Location
import java.util.*

data class Expense(
    val id: String,
    val name: String,
    val owner: User,
    val participants: List<User>,
    val location: Location,
    val creationDate: Date,
    val dueDate: Date,
    val expenseAmount: Currency,
    val participantExpensePercentage: List<Double>,
    val isPaid: List<Boolean>,
    //val isArchived: Boolean
)
