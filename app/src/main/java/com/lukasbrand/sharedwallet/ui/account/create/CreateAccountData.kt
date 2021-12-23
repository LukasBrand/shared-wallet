package com.lukasbrand.sharedwallet.ui.account.create

import android.provider.ContactsContract

data class CreateAccountData(
    var name: String = "",
    var email: String = "",
    var password: String = ""
)
