package com.lukasbrand.sharedwallet.ui.wallet.list.item

import android.content.res.Resources
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.data.Result
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.ui.wallet.create.CreateExpenseViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun convertTimestampToFormatted(time: LocalDateTime, resources: Resources): CharSequence {
    val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
    return time.format(formatter)
}

@BindingAdapter("expenseNameInfo")
fun EditText.setNameInfo(expense: Result<Expense>) {
    val name: String = when (expense) {
        is Result.Success -> expense.data.name
        else -> ""
    }.exhaustive
    setText(name)
}

@BindingAdapter("expenseCreationDateInfo")
fun TextView.setCreationDateInfo(expense: Result<Expense>) {
    text = when (expense) {
        is Result.Success -> convertTimestampToFormatted(
            expense.data.creationDate,
            context.resources
        )
        else -> ""
    }.exhaustive
}

@BindingAdapter("expenseDueDateInfo")
fun TextView.setDueDateInfo(expense: Result<Expense>) {
    text = when (expense) {
        is Result.Success -> convertTimestampToFormatted(expense.data.dueDate, context.resources)
        else -> ""
    }.exhaustive
}

@BindingAdapter("expenseCustomPriceInfo")
fun EditText.setCustomPriceInfo(expense: Result<Expense>) {
    val price: String = when (expense) {
        is Result.Success -> expense.data.expenseAmount.toPlainString()
        else -> ""
    }.exhaustive
    setText(price)
}

@BindingAdapter("expenseValidParticipantInfo")
fun Button.setValidParticipantInfo(potentialParticipant: Result<ExpenseParticipant>) {
    isEnabled = when (potentialParticipant) {
        is Result.Success -> true
        is Result.Error -> false
        Result.Loading -> false
    }.exhaustive
}
