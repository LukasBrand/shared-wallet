package com.lukasbrand.sharedwallet.data.model

import android.location.Location
import java.math.BigDecimal
import java.util.*

data class ExpenseApiModel(
    val id: String?,
    val name: String,
    val owner: String,
    val location: Location,
    val creationDate: Date,
    val dueDate: Date,
    val expenseAmount: BigDecimal,
    val participants: List<String>,
    val participantExpensePercentage: List<Double>,
    val hasPaid: List<Boolean>,
    //val isArchived: Boolean
)