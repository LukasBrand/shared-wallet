package com.lukasbrand.sharedwallet.database

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreDatabase(val firebaseFirestore: FirebaseFirestore) {

    val expensesDao: ExpensesDao = ExpensesDao()
    val participantsDao : ParticipantsDao = ParticipantsDao()

    companion object {

        @Volatile
        private var INSTANCE: FirestoreDatabase? = null

        fun getInstance(context: Context) : FirestoreDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = FirestoreDatabase(FirebaseFirestore.getInstance())
                }

                return instance
            }
        }
    }

}