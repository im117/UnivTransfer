package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "college")
data class College(
    @PrimaryKey @ColumnInfo(name = "college_id") val collegeId: Int,
    @ColumnInfo(name = "college_name") val collegeName: String,
    @ColumnInfo(name = "is_four_year") val isFourYear: Boolean
)