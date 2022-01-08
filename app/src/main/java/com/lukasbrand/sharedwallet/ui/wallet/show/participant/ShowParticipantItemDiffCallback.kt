package com.lukasbrand.sharedwallet.ui.wallet.show.participant

import androidx.recyclerview.widget.DiffUtil
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.ExpenseParticipant

class ShowParticipantItemDiffCallback : DiffUtil.ItemCallback<ExpenseParticipant>() {

    override fun areItemsTheSame(oldItem: ExpenseParticipant, newItem: ExpenseParticipant): Boolean {
        return oldItem.user.id == newItem.user.id
    }

    override fun areContentsTheSame(oldItem: ExpenseParticipant, newItem: ExpenseParticipant): Boolean {
        return oldItem == newItem
    }
}