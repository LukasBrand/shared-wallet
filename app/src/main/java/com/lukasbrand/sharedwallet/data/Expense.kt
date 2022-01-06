package com.lukasbrand.sharedwallet.data

import android.location.Location
import com.google.type.LatLng
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class Expense(
    val id: String = "",
    val name: String = "",
    val owner: User,
    val location: LatLng = LatLng.getDefaultInstance(),
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val dueDate: LocalDateTime = LocalDateTime.now(),
    val expenseAmount: BigDecimal = BigDecimal("0"),
    val participants: List<ExpenseParticipant>,
    //val isArchived: Boolean
)
