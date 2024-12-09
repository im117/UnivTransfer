package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


// Note: some ideas for this are adapted from
// https://developer.android.com/topic/libraries/architecture/viewmodel
data class MainState(
    val twoYearCollege: College? = null,
    val courseList: List<Course> = emptyList(),
    val transferDestination: College? = null,
)

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainState())
    val uiState = _uiState.asStateFlow()

    fun updateState(
        twoYearCollege: College? = null,
        courseList: List<Course>? = null,
        transferDestination: College? = null,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                twoYearCollege = twoYearCollege ?: currentState.twoYearCollege,
                courseList = courseList ?: currentState.courseList,
                transferDestination = transferDestination ?: currentState.transferDestination,
            )
        }
    }
}