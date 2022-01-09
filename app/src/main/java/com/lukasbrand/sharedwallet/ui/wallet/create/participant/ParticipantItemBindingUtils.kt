package com.lukasbrand.sharedwallet.ui.wallet.create.participant

import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.data.ExpenseParticipant


@BindingAdapter("participantItemImage")
fun ImageView.setParticipantImage(item: ExpenseParticipant?) {
    item?.let {
        setImageBitmap(item.user.image)
    }
}

@BindingAdapter("participantItemName")
fun TextView.setParticipantName(item: ExpenseParticipant?) {
    item?.let {
        text = item.user.name
    }
}

@BindingAdapter("participantItemPercentageShow")
fun TextView.setParticipantPercentage(item: ExpenseParticipant?) {
    item?.let {
        text = item.expensePercentage.toString()
    }
}

@BindingAdapter("participantItemHasPaid")
fun CheckBox.setParticipantHasPaid(item: ExpenseParticipant?) {
    item?.let {
        isChecked = item.hasPaid
    }
}

@BindingAdapter("participantItemPercentage")
fun SeekBar.setPercentage(item: ExpenseParticipant?) {
    item?.let {
        progress = item.expensePercentage
    }
}

@BindingAdapter("participantBackgroundHasPaid")
fun TextView.setBackground(item: ExpenseParticipant?) {
    item?.let {
        if (item.hasPaid) {
            background.setTint(resources.getColor(R.color.red, null))
        } else {
            background.setTint(resources.getColor(R.color.black, null))
        }
    }
}

