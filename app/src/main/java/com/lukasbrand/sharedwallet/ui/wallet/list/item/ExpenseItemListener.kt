package com.lukasbrand.sharedwallet.ui.wallet.list.item

import com.lukasbrand.sharedwallet.data.Expense

class ExpenseItemListener(val clickListener: (expense: Expense) -> Unit) {
    fun onClick(expense: Expense) = clickListener(expense)
}