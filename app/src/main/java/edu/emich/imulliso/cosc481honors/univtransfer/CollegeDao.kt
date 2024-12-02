package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CollegeDao {
    @Query("SELECT * FROM college")
    suspend fun getAll(): List<College>

    @Query("SELECT * FROM college WHERE is_four_year = 0")
    suspend fun getTwoYear(): List<College>

    @Query(
        """
        SELECT * FROM college
        WHERE LOWER(college_name) LIKE '%' || LOWER(:query) || '%'
        ORDER BY college_name
        LIMIT 5
    """
    )
    suspend fun searchTop5(query: String): List<College>

    @Query(
        """
        SELECT * FROM college
        WHERE LOWER(college_name) LIKE '%' || LOWER(:query) || '%'
            AND is_four_year = 0
        ORDER BY college_name
        LIMIT 5
    """
    )
    suspend fun searchTop5TwoYear(query: String): List<College>

    @Query(
        """
        SELECT * FROM college
        WHERE LOWER(college_name) LIKE '%' || LOWER(:query) || '%'
            AND is_four_year = 1
        ORDER BY college_name
        LIMIT 5
    """
    )
    suspend fun searchTop5FourYear(query: String): List<College>

    @Query("SELECT * FROM college WHERE is_four_year = 1")
    suspend fun getFourYear(): List<College>

}