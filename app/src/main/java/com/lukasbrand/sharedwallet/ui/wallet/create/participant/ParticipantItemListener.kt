package com.lukasbrand.sharedwallet.ui.wallet.create.participant

import com.lukasbrand.sharedwallet.data.ExpenseParticipant

class ParticipantItemListener(val clickListener: (expenseParticipantId: String) -> Unit) {
    fun onClick(expenseParticipant: ExpenseParticipant) = clickListener(expenseParticipant.user.id)
}