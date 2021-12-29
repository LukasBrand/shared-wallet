package com.lukasbrand.sharedwallet.data.model

import android.location.Location
import java.util.*

data class ExpenseApiModel(
    val id: String?,
    val name: String,
    val owner: String,
    val participants: List<String>,
    val location: Location,
    val creationDate: Date,
    val dueDate: Date,
    val expenseAmount: Currency,
    val participantExpensePercentage: List<Double>,
    val isPaid: List<Boolean>,
    //val isArchived: Boolean
)