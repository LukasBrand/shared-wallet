package com.lukasbrand.sharedwallet.binding

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.types.Result
import com.lukasbrand.sharedwallet.types.UiState
import com.lukasbrand.sharedwallet.utils.convertTimestampToFormatted
import java.time.LocalDateTime

@BindingAdapter("expensePotentialEmailInfo")
fun TextView.setPotentialEmail(email: UiState<String>) {
    text = when (email) {
        is UiState.Set -> email.data
        UiState.Unset -> ""
    }.exhaustive
}

@BindingAdapter("expenseCreationDateInfo")
fun TextView.setCreationDateInfo(creationDate: UiState<LocalDateTime>) {
    text = when (creationDate) {
        is UiState.Set -> convertTimestampToFormatted(creationDate.data, context.resources)
        UiState.Unset -> "Creation Date"
    }.exhaustive
}

@BindingAdapter("expenseDueDateInfo")
fun TextView.setDueDateInfo(dueDate: UiState<LocalDateTime>) {
    text = when (dueDate) {
        is UiState.Set -> convertTimestampToFormatted(dueDate.data, context.resources)
        UiState.Unset -> "Due Date"
    }.exhaustive
}

@BindingAdapter("expenseCustomPriceInfo")
fun EditText.setCustomPriceInfo(expenseAmount: String) {
    setText(expenseAmount)
}

@BindingAdapter("expenseValidParticipantInfo")
fun Button.setValidParticipantInfo(potentialParticipant: Result<ExpenseParticipant>) {
    isEnabled = when (potentialParticipant) {
        is Result.Success -> true
        is Result.Error -> false
        Result.Loading -> false
    }.exhaustive
}
