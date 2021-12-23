package com.lukasbrand.sharedwallet.ui.wallet.list.item

import com.lukasbrand.sharedwallet.data.Expense

class ExpenseItemListener(val clickListener: (expenseId: Long) -> Unit) {
    fun onClick(expense: Expense) = clickListener(expense.id)
}