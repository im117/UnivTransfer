package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TransferEquivDao {

    @Query(
        """
            SELECT c.college_id FROM transfer_equiv AS t
                INNER JOIN course AS c ON t.four_year_course_id = c.course_id
                
                WHERE t.two_year_course_id = :courseId
        """
    )
    suspend fun getCollegeIdsWithEquiv(courseId: Int): List<Int>


    @Query(
        """
        SELECT c.* FROM course AS c
                INNER JOIN transfer_equiv AS t ON t.four_year_course_id = c.course_id
                WHERE t.two_year_course_id = :courseId AND c.college_id = :collegeId
    """
    )
    suspend fun getEquivalentCoursesInCollege(courseId: Int, collegeId: Int): List<Course>
}

