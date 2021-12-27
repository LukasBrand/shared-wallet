package com.lukasbrand.sharedwallet.data.model

import android.location.Location
import java.util.*

data class ExpenseApiModel(
    val id: String,
    val name: String,
    val owner: Long,
    val participants: List<Long>,
    val location: Location,
    val creationDate: Date,
    val dueDate: Date,
    val individualExpense: List<Currency>,
    val isPaid: List<Boolean>,
    val isArchived: Boolean
)