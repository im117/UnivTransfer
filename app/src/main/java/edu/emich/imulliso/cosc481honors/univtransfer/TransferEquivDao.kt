package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class TransferEquivDao {
    @Query(
        """
        SELECT * FROM transfer_equiv
                WHERE two_year_course_id = :courseId OR four_year_course_id = :courseId
        """
    )
    abstract fun getAllEquivalencyRecords(courseId: Int): List<TransferEquiv>


}