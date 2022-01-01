package com.lukasbrand.sharedwallet.data

data class ExpenseParticipant(
    val user: User,
    val expensePercentage: Int,
    val hasPaid: Boolean
)
