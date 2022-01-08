package com.lukasbrand.sharedwallet.ui.wallet.show.participant

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.data.ExpenseParticipant


@BindingAdapter("participantItemOwner")
fun ConstraintLayout.setParticipantOwner(item: ExpenseParticipant?) {
    item?.let {
        if (item.isOwner) {
            setBackgroundColor(resources.getColor(R.color.owner_background, null))
        }
    }
}

@BindingAdapter("participantItemImage")
fun ImageView.setParticipantImage(item: ExpenseParticipant?) {
    item?.let {
        setImageBitmap(item.user.image)
    }
}

@BindingAdapter("participantItemInitials")
fun TextView.setParticipantInitials(item: ExpenseParticipant?) {
    item?.let {
        text = item.user.name[0].toString()
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
fun ImageView.setParticipantHasPaid(item: ExpenseParticipant?) {
    item?.let {
        if (item.hasPaid) {
            setImageResource(R.drawable.ic_baseline_check_24)
        } else {
            setImageResource(R.drawable.ic_baseline_clear_24)
        }
    }
}

@BindingAdapter("participantItemPercentage")
fun ProgressBar.setPercentage(item: ExpenseParticipant?) {
    item?.let {
        progress = item.expensePercentage
    }
}