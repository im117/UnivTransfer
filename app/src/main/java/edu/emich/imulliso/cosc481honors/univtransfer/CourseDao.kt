package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CourseDao {
    @Query("SELECT * FROM course WHERE course_id = :courseId")
    suspend fun lookupCourseById(courseId: Int): Course

    @Query(
        """
        SELECT * FROM course 
        WHERE LOWER(subject_code || ' ' || course_number) LIKE '%' || LOWER(:query) || '%'
            AND college_id = :collegeId
        ORDER BY subject_code, course_number
        """
    )
    suspend fun searchCoursesInCollege(query: String, collegeId: Int): List<Course>
}