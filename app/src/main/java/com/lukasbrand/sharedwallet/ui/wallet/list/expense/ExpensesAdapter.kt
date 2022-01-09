package com.lukasbrand.sharedwallet.ui.wallet.list.expense

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.ui.wallet.list.expense.participant.ParticipantItemListener
import com.lukasbrand.sharedwallet.ui.wallet.list.expense.participant.ParticipantsAdapter

class ExpensesAdapter(private val clickListener: ExpenseItemListener) :
    ListAdapter<Expense, ExpenseItemViewHolder>(ExpenseItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseItemViewHolder {
        return ExpenseItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ExpenseItemViewHolder, position: Int) {
        val item = getItem(position)
        val participantsAdapter = ParticipantsAdapter(ParticipantItemListener {
            clickListener.onClick(item)
        })
        holder.bind(item, clickListener, participantsAdapter)
        participantsAdapter.submitList(item.participants)
    }
}

