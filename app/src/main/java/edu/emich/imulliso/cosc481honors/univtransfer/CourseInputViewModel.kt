package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel


// Note: some ideas for this are adapted from
// https://developer.android.com/codelabs/jetpack-compose-state?index=..%2F..index#11


class CourseInputViewModel : ViewModel() {
    private val _courses = emptyList<Course>().toMutableStateList()
    val courses: List<Course> get() = _courses


    fun removeCourse(course: Course) {
        _courses.remove(course)
    }

    fun addCourse(course: Course) {
        _courses.add(course)
    }
}