package com.lukasbrand.sharedwallet.ui.wallet.list.expense.participant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lukasbrand.sharedwallet.data.ExpenseParticipant
import com.lukasbrand.sharedwallet.databinding.CreateExpenseParticipantsItemBinding
import com.lukasbrand.sharedwallet.databinding.ListExpensesItemParticipantBinding
import com.lukasbrand.sharedwallet.ui.wallet.list.expense.ExpenseItemListener

/**
 * Creates an item view holder which represents an item inside the expenses list (recycler view).
 * It uses data binding to access the resources and a binding adapter to convert the expense into its parts.
 *
 * @author Lukas Brand
 */
class ParticipantItemViewHolder private constructor(val binding: ListExpensesItemParticipantBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): ParticipantItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ListExpensesItemParticipantBinding.inflate(layoutInflater, parent, false)
            return ParticipantItemViewHolder(binding)
        }
    }

    fun bind(item: ExpenseParticipant, clickListener: ParticipantItemListener) {
        binding.participant = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}
