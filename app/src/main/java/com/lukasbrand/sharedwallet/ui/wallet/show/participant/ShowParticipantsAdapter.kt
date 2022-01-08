package com.lukasbrand.sharedwallet.ui.wallet.show.participant

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lukasbrand.sharedwallet.data.ExpenseParticipant

class ShowParticipantsAdapter :
    ListAdapter<ExpenseParticipant, ShowParticipantItemViewHolder>(ShowParticipantItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowParticipantItemViewHolder {
        return ShowParticipantItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ShowParticipantItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
