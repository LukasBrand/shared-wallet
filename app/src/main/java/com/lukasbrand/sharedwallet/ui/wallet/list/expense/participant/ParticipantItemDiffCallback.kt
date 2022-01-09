package com.lukasbrand.sharedwallet.ui.wallet.list.expense.participant

import androidx.recyclerview.widget.DiffUtil
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.data.ExpenseParticipant

class ParticipantItemDiffCallback : DiffUtil.ItemCallback<ExpenseParticipant>() {

    override fun areItemsTheSame(oldItem: ExpenseParticipant, newItem: ExpenseParticipant): Boolean {
        return oldItem.user.id == newItem.user.id
    }

    override fun areContentsTheSame(oldItem: ExpenseParticipant, newItem: ExpenseParticipant): Boolean {
        return oldItem == newItem
    }
}