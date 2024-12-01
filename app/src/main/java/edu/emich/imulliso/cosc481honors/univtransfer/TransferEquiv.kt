package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.Entity

@Entity(tableName = "transfer_equiv", primaryKeys = ["course1Id", "course2Id"])
data class TransferEquiv(
    val course1Id: Int,
    val course2Id: Int,
)