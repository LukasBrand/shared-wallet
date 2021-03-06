package com.lukasbrand.sharedwallet.data.datasource.firestore

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.lukasbrand.sharedwallet.exception.NotLoggedInException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthApi(private val firebaseAuth: FirebaseAuth) {

    companion object {

        @Volatile
        private var INSTANCE: FirebaseAuthApi? = null

        fun getInstance(): FirebaseAuthApi {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = FirebaseAuthApi(FirebaseAuth.getInstance())
                }

                return instance
            }
        }
    }

    suspend fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): String = suspendCancellableCoroutine { continuation ->
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                continuation.resume(result.user!!.uid)
            }.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }.addOnCanceledListener {
                continuation.cancel()
            }
    }

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): String = suspendCancellableCoroutine { continuation ->
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                continuation.resume(result.user!!.uid)
            }.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }.addOnCanceledListener {
                continuation.cancel()
            }
    }

    suspend fun signOut(): Unit = suspendCancellableCoroutine { continuation ->
        firebaseAuth.signOut()
        continuation.resume(Unit)
    }

    suspend fun getAuthStatus(): Boolean = suspendCancellableCoroutine { continuation ->
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            currentUser.reload()
                /* This is currently bugged
                .addOnSuccessListener {
                    continuation.resume(true)
                */
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        continuation.resume(true)
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resume(false)
                }.addOnCanceledListener {
                    continuation.cancel()
                }
        } else {
            continuation.resume(false)
        }
    }

    suspend fun getUserId(): String = suspendCoroutine { continuation ->
        if (firebaseAuth.currentUser == null) {
            continuation.resumeWithException(NotLoggedInException())
        } else {
            continuation.resume(firebaseAuth.currentUser!!.uid)
        }
    }

    suspend fun updateEmailAddress(oldEmail: String, oldPassword: String, newEmail: String): Unit =
        suspendCancellableCoroutine { continuation ->
            val credential = EmailAuthProvider.getCredential(oldEmail, oldPassword)
            firebaseAuth.currentUser!!.reauthenticate(credential)
                /* This is currently bugged
                .addOnSuccessListener {
                    continuation.resume(true)
                */
                .addOnCompleteListener { reauthenticateSuccess ->
                    if (reauthenticateSuccess.isSuccessful) {
                        firebaseAuth.currentUser!!.updateEmail(newEmail)
                            /*.addOnSuccessListener {
                                continuation.resume(Unit)
                            }*/
                            .addOnCompleteListener { updateSuccess ->
                                if (updateSuccess.isSuccessful) {
                                    continuation.resume(Unit)
                                }
                            }.addOnFailureListener { exception ->
                                continuation.resumeWithException(exception)
                            }.addOnCanceledListener {
                                continuation.cancel()
                            }
                    }
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }.addOnCanceledListener {
                    continuation.cancel()
                }
        }

    suspend fun updatePassword(oldEmail: String, oldPassword: String, newPassword: String): Unit =
        suspendCancellableCoroutine { continuation ->
            val credential = EmailAuthProvider.getCredential(oldEmail, oldPassword)
            firebaseAuth.currentUser!!.reauthenticate(credential)
                /* This is currently bugged
                .addOnSuccessListener {
                    continuation.resume(true)
                */
                .addOnCompleteListener { reauthenticateSuccess ->
                    if (reauthenticateSuccess.isSuccessful) {
                        firebaseAuth.currentUser!!.updatePassword(newPassword)
                            /*.addOnSuccessListener {
                                continuation.resume(Unit)
                            }*/
                            .addOnCompleteListener { updateSuccess ->
                                if (updateSuccess.isSuccessful) {
                                    continuation.resume(Unit)
                                }
                            }.addOnFailureListener { exception ->
                                continuation.resumeWithException(exception)
                            }.addOnCanceledListener {
                                continuation.cancel()
                            }
                    }
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }.addOnCanceledListener {
                    continuation.cancel()
                }
        }

    suspend fun deleteUser(email: String, password: String): Unit =
        suspendCancellableCoroutine { continuation ->
            val credential = EmailAuthProvider.getCredential(email, password)
            firebaseAuth.currentUser!!.reauthenticate(credential)
                /* This is currently bugged
                .addOnSuccessListener {
                    continuation.resume(true)
                */
                .addOnCompleteListener { reauthenticateSuccess ->
                    if (reauthenticateSuccess.isSuccessful) {
                        firebaseAuth.currentUser!!.delete()
                            /*.addOnSuccessListener {
                                continuation.resume(Unit)
                            }*/
                            .addOnCompleteListener { deleteSuccessful ->
                                if (deleteSuccessful.isSuccessful) {
                                    continuation.resume(Unit)
                                }
                            }.addOnFailureListener { exception ->
                                continuation.resumeWithException(exception)
                            }.addOnCanceledListener {
                                continuation.cancel()
                            }
                    }
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }.addOnCanceledListener {
                    continuation.cancel()
                }
        }
}