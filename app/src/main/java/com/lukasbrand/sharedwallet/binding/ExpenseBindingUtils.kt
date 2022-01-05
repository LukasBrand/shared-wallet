package com.lukasbrand.sharedwallet.ui.wallet.list.item

import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.Result
import com.lukasbrand.sharedwallet.exhaustive
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@BindingAdapter("expenseNameInfo")
fun TextView.setNameInfo(item: Result<Expense>) {
    text = when (item) {
        is Result.Success -> item.data.name
        else -> ""
    }.exhaustive
}

@BindingAdapter("expenseCreationDateInfo")
fun TextView.setCreationDateInfo(item: Result<Expense>) {
    text = when (item) {
        is Result.Success -> convertTimestampToFormatted(item.data.creationDate, context.resources)
        else -> ""
    }.exhaustive
}

@BindingAdapter("expenseDueDateInfo")
fun TextView.setDueDateInfo(item: Result<Expense>) {
    text = when (item) {
        is Result.Success -> convertTimestampToFormatted(item.data.dueDate, context.resources)
        else -> ""
    }.exhaustive
}


fun convertTimestampToFormatted(time: LocalDateTime, resources: Resources): CharSequence {
    val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
    return time.format(formatter)
}
