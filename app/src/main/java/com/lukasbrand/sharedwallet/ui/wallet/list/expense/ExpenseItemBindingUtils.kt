package com.lukasbrand.sharedwallet.ui.wallet.list.expense

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.utils.convertTimestampToFormatted


@BindingAdapter("expenseItemOwnerImage")
fun ImageView.setOwnerImage(item: Expense?) {
    item?.let {
        setImageBitmap(item.owner.image)
    }
}

@BindingAdapter("ownerInitials")
fun TextView.setOwnerInitials(item: User?) {
    item?.let {
        text = item.name[0].toString()
    }
}

@BindingAdapter("expenseItemCreateInfo")
fun TextView.setCreateInfo(item: Expense?) {
    item?.let {
        text = convertTimestampToFormatted(item.creationDate, context.resources)
    }
}

@BindingAdapter("expenseItemEndInfo")
fun TextView.setEndInfo(item: Expense?) {
    item?.let {
        text = convertTimestampToFormatted(item.dueDate, context.resources)
    }
}

@BindingAdapter("expenseItemCountInfo")
fun TextView.setPaidCountInfo(item: Expense?) {
    item?.let {
        text = context.getString(R.string.expense_item_paid_count, item.participants.stream().filter { it.hasPaid }.count(), item.participants.size)
    }
}