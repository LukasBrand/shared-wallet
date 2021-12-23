package com.lukasbrand.sharedwallet.ui.account.create

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lukasbrand.sharedwallet.data.Expense

class CreateAccountViewModel : ViewModel() {

    private val _accountData = MutableLiveData<CreateAccountData>()

    val accountData
        get() = _accountData
}