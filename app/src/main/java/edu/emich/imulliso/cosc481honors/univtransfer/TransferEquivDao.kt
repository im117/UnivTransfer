package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class TransferEquivDao {
    @Query(
        """
        SELECT * FROM transfer_equiv
                WHERE course1Id = :courseId OR course2Id = :courseId
        """
    )
    abstract fun getAllEquivalencyRecords(courseId: Int): List<TransferEquiv>


}