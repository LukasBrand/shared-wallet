package com.lukasbrand.sharedwallet.data

data class ExpenseParticipant(
    val user: User,
    val expensePercentage: Double,
    val hasPaid: Boolean
)
