package com.lukasbrand.sharedwallet.data

import com.google.type.LatLng
import java.math.BigDecimal
import java.time.LocalDateTime

data class NewExpense(
    val id: String? = null,
    val name: String? = null,
    val owner: User,
    val location: LatLng? = null,
    val creationDate: LocalDateTime? = null,
    val dueDate: LocalDateTime? = null,
    val expenseAmount: BigDecimal? = null,
    val participants: List<ExpenseParticipant>,
    //val isArchived: Boolean
) {
    fun isComplete(): Boolean =
        id != null && name != null && location != null && creationDate != null && dueDate != null && expenseAmount != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewExpense

        if (id != other.id) return false
        if (name != other.name) return false
        if (owner != other.owner) return false
        if (location != other.location) return false
        if (creationDate != other.creationDate) return false
        if (dueDate != other.dueDate) return false
        if (expenseAmount != other.expenseAmount) return false
        if (participants != other.participants) return false
        if (participants.toTypedArray()
                .contentDeepEquals(other.participants.toTypedArray())
        ) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + owner.hashCode()
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (creationDate?.hashCode() ?: 0)
        result = 31 * result + (dueDate?.hashCode() ?: 0)
        result = 31 * result + (expenseAmount?.hashCode() ?: 0)
        result = 31 * result + participants.hashCode()
        return result
    }


}
