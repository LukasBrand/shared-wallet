package com.lukasbrand.sharedwallet.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.NewExpense
import java.time.ZoneId
import java.util.*

data class ExpenseApiModel(
    val id: String?,
    val name: String,
    val owner: String,
    val location: GeoPoint,
    val creationDate: Timestamp,
    val dueDate: Timestamp,
    val expenseAmount: String,
    val participants: List<String>,
    val participantExpensePercentage: List<Int>,
    val hasPaid: List<Boolean>,
    //val isArchived: Boolean
) {
    //no arg constructor for serialization
    constructor() : this(
        null,
        "",
        "owner",
        GeoPoint(0.0, 0.0),
        Timestamp.now(),
        Timestamp.now(),
        "",
        listOf(),
        listOf(),
        listOf()
    )

    constructor(expense: Expense) : this(
        expense.id,
        expense.name,
        expense.owner.id,
        GeoPoint(expense.location.latitude, expense.location.longitude),
        Timestamp(Date.from(expense.creationDate.atZone(ZoneId.systemDefault()).toInstant())),
        Timestamp(Date.from(expense.dueDate.atZone(ZoneId.systemDefault()).toInstant())),
        expense.expenseAmount.toPlainString(),
        expense.participants.map { participant -> participant.user.id },
        expense.participants.map { participant -> participant.expensePercentage },
        expense.participants.map { participant -> participant.hasPaid })

    constructor(expense: NewExpense) : this(
        expense.id,
        expense.name!!,
        expense.owner.id,
        GeoPoint(expense.location!!.latitude, expense.location.longitude),
        Timestamp(Date.from(expense.creationDate!!.atZone(ZoneId.systemDefault()).toInstant())),
        Timestamp(Date.from(expense.dueDate!!.atZone(ZoneId.systemDefault()).toInstant())),
        expense.expenseAmount!!.toPlainString(),
        expense.participants.map { participant -> participant.user.id },
        expense.participants.map { participant -> participant.expensePercentage },
        expense.participants.map { participant -> participant.hasPaid })
}