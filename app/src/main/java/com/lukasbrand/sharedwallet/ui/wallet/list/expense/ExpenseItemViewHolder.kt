package com.lukasbrand.sharedwallet.ui.wallet.list.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.databinding.ListExpenseItemBinding
import com.lukasbrand.sharedwallet.ui.wallet.list.expense.participant.ParticipantsAdapter

/**
 * Creates an item view holder which represents an item inside the expenses list (recycler view).
 * It uses data binding to access the resources and a binding adapter to convert the expense into its parts.
 *
 * @author Lukas Brand
 */
class ExpenseItemViewHolder private constructor(val binding: ListExpenseItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): ExpenseItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListExpenseItemBinding.inflate(layoutInflater, parent, false)
            return ExpenseItemViewHolder(binding)
        }
    }

    fun bind(
        item: Expense,
        clickListener: ExpenseItemListener,
        participantsAdapter: ParticipantsAdapter
    ) {
        binding.expense = item
        binding.clickListener = clickListener
        binding.participants.adapter = participantsAdapter
        binding.executePendingBindings()
    }
}
