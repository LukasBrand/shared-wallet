package com.lukasbrand.sharedwallet.data

import android.location.Location
import java.math.BigDecimal
import java.util.*

data class Expense(
    val id: String,
    val name: String,
    val owner: User,
    val location: Location,
    val creationDate: Date,
    val dueDate: Date,
    val expenseAmount: BigDecimal,
    val participants: List<ExpenseParticipant>,
    //val isArchived: Boolean
)
