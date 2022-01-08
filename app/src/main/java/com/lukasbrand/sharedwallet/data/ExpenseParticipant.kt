package com.lukasbrand.sharedwallet.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExpenseParticipant(
    val user: User,
    val expensePercentage: Int,
    val hasPaid: Boolean,
    val isOwner: Boolean
) : Parcelable
