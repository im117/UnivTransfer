package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "transfer_equiv", primaryKeys = ["two_year_course_id", "four_year_course_id"])
data class TransferEquiv(
    @ColumnInfo(name = "two_year_course_id") val twoYearCourseId: Int,
    @ColumnInfo(name = "four_year_course_id") val fourYearCourseId: Int,
)