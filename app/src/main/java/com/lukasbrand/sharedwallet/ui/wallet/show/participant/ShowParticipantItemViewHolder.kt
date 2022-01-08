package com.lukasbrand.sharedwallet.ui.wallet.show.participant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.databinding.ShowExpenseParticipantsItemBinding

/**
 * Creates an item view holder which represents an item inside the expenses list (recycler view).
 * It uses data binding to access the resources and a binding adapter to convert the expense into its parts.
 *
 * @author Lukas Brand
 */
class ShowParticipantItemViewHolder private constructor(val binding: ShowExpenseParticipantsItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): ShowParticipantItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ShowExpenseParticipantsItemBinding.inflate(layoutInflater, parent, false)
            return ShowParticipantItemViewHolder(
                binding
            )
        }
    }

    fun bind(item: ExpenseParticipant) {
        binding.participant = item
        binding.executePendingBindings()
    }
}
