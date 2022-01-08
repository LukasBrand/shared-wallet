package com.lukasbrand.sharedwallet.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal
import java.time.LocalDateTime

@Parcelize
data class Expense(
    val id: String,
    val name: String,
    val owner: User,
    val location: LatLng,
    val creationDate: LocalDateTime,
    val dueDate: LocalDateTime,
    val expenseAmount: BigDecimal,
    val participants: List<ExpenseParticipant>,
    //val isArchived: Boolean
) : Parcelable
