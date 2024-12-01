package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course")
data class Course(
    @PrimaryKey @ColumnInfo(name = "course_id") val courseId: Int,
    @ColumnInfo(name = "college_id") val collegeId: Int,
    @ColumnInfo(name = "course_name") val courseName: String,
    @ColumnInfo(name = "subject_code") val subjectCode: String,
    @ColumnInfo(name = "course_number") val courseNumber: String, // string in case of letter like 481W
)