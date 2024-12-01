package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [College::class, Course::class, TransferEquiv::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collegeDao(): CollegeDao
    abstract fun courseDao(): CourseDao
    abstract fun transferEquivDao(): TransferEquivDao
}