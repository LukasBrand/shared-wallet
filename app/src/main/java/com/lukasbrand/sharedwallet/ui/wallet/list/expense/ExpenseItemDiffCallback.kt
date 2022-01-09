package com.lukasbrand.sharedwallet.ui.wallet.list.expense

import androidx.recyclerview.widget.DiffUtil
import com.lukasbrand.sharedwallet.data.Expense

class ExpenseItemDiffCallback : DiffUtil.ItemCallback<Expense>() {

    override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
        return oldItem == newItem
    }
}