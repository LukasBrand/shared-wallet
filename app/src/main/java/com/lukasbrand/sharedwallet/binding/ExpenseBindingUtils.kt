package com.lukasbrand.sharedwallet.ui.wallet.list.item

import android.content.res.Resources
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.data.NewExpense
import com.lukasbrand.sharedwallet.data.Result
import com.lukasbrand.sharedwallet.exhaustive
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun convertTimestampToFormatted(time: LocalDateTime, resources: Resources): CharSequence {
    val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
    return time.format(formatter)
}

@BindingAdapter("expenseCreationDateInfo")
fun TextView.setCreationDateInfo(expense: Result<NewExpense>) {
    text = when (expense) {
        is Result.Success -> expense.data.creationDate?.let {
            convertTimestampToFormatted(it, context.resources)
        } ?: "Creation Date"
        else -> ""
    }.exhaustive
}

@BindingAdapter("expenseDueDateInfo")
fun TextView.setDueDateInfo(expense: Result<NewExpense>) {
    text = when (expense) {
        is Result.Success -> expense.data.dueDate?.let {
            convertTimestampToFormatted(it, context.resources)
        } ?: "Due Date"
        else -> ""
    }.exhaustive
}

@BindingAdapter("expenseCustomPriceInfo")
fun EditText.setCustomPriceInfo(expense: Result<NewExpense>) {
    val price: String = when (expense) {
        is Result.Success -> expense.data.expenseAmount?.toPlainString() ?: ""
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
