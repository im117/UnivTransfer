package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CollegeDao {
    @Query("SELECT * FROM college")
    fun getAll(): List<College>

    @Query("SELECT * FROM college WHERE is_four_year = 0")
    fun getTwoYear(): List<College>

    @Query(
        """
        SELECT * FROM college
        WHERE college_name LIKE '%'+:query+'%'
        ORDER BY college_name
        LIMIT 5
    """
    )
    fun searchTop5(query: String): List<College>


    @Query("SELECT * FROM college WHERE is_four_year = 1")
    fun getFourYear(): List<College>

}