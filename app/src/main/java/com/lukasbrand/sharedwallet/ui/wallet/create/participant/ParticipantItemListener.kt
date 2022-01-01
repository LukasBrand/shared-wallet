package com.lukasbrand.sharedwallet.ui.wallet.create.participant

import com.lukasbrand.sharedwallet.data.ExpenseParticipant

class ParticipantItemListener(
    private val onPaidListener: (isPaid: Boolean) -> Unit,
    private val onPercentageChangedListener: (percent: Int) -> Unit,
    private val onParticipantRemoveListener: (participantId: String) -> Unit
) {
    fun onPaid(isPaid: Boolean) = onPaidListener(isPaid)

    fun onPercentageChanged(percent: Int) = onPercentageChangedListener(percent)

    fun onParticipantRemove(participantId: String) = onParticipantRemoveListener(participantId)
}