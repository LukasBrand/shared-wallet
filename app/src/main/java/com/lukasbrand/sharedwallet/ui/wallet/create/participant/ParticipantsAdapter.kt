package com.lukasbrand.sharedwallet.ui.wallet.create.participant

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lukasbrand.sharedwallet.data.ExpenseParticipant

class ParticipantsAdapter(val clickListener: ParticipantItemListener) :
    ListAdapter<ExpenseParticipant, ParticipantItemViewHolder>(ParticipantItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantItemViewHolder {
        return ParticipantItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ParticipantItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}
