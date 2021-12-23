package com.lukasbrand.sharedwallet.ui.wallet.list.item

import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.data.Expense
import java.util.*


@BindingAdapter("expenseItemOwnerImage")
fun ImageView.setOwnerImage(item: Expense?) {
    item?.let {
        setImageBitmap(item.owner.image)
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
        text = context.getString(R.string.expense_item_paid_count, item.isPaid.stream().filter { it }.count(), item.isPaid.size)
    }
}


private fun convertTimestampToFormatted(time: Date, resources: Resources?): CharSequence? {
    TODO("Not yet implemented")
}
