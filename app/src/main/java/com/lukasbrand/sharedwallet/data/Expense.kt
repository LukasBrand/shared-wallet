package com.lukasbrand.sharedwallet.data

import android.location.Location
import java.util.*

data class Expense(
    val id: Long,
    val name: String,
    val owner: Participant,
    val participants: List<Participant>,
    val location: Location,
    val creationDate: Date,
    val dueDate: Date,
    val individualExpense: List<Int>,
    val isPaid: List<Boolean>
)
