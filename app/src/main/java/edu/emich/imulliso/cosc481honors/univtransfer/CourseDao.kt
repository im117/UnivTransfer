package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CourseDao {
    @Query("SELECT * FROM course WHERE course_id = :courseId")
    fun lookupCourseById(courseId: Int): Course
}