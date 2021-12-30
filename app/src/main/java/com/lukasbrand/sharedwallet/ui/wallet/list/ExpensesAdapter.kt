package com.lukasbrand.sharedwallet.ui.wallet.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.ui.wallet.list.item.ExpenseItemDiffCallback
import com.lukasbrand.sharedwallet.ui.wallet.list.item.ExpenseItemListener
import com.lukasbrand.sharedwallet.ui.wallet.list.item.ExpenseItemViewHolder

class ExpensesAdapter(val clickListener: ExpenseItemListener) :
    ListAdapter<Expense, ExpenseItemViewHolder>(ExpenseItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseItemViewHolder {
        return ExpenseItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ExpenseItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

