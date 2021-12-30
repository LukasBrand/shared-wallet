package com.lukasbrand.sharedwallet.ui.wallet.create.participant

import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import java.util.*


@BindingAdapter("expenseItemUserImage")
fun ImageView.setUserImage(item: ExpenseParticipant?) {
    item?.let {
        setImageBitmap(item.user.image)
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


private fun convertTimestampToFormatted(time: Date, resources: Resources?): CharSequence? {
    TODO("Not yet implemented")
}
