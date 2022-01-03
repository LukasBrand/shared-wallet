package com.lukasbrand.sharedwallet.ui.wallet.create.participant

import com.lukasbrand.sharedwallet.data.ExpenseParticipant

class ParticipantItemListener(
    private val onPaidListener: (participantId:String, isPaid: Boolean) -> Unit,
    private val onPercentageChangedListener: (participantId:String, percent: Int) -> Unit,
    private val onParticipantRemoveListener: (participantId: String) -> Unit
) {
    fun onPaid(participantId:String, isPaid: Boolean) = onPaidListener(participantId, isPaid)

    fun onPercentageChanged(participantId:String, percent: Int) = onPercentageChangedListener(participantId, percent)

    fun onParticipantRemove(participantId: String) = onParticipantRemoveListener(participantId)
}