package com.lukasbrand.sharedwallet.ui.wallet.list.expense.participant

class ParticipantItemListener(val clickListener: () -> Unit) {
    fun onClick() = clickListener()
}