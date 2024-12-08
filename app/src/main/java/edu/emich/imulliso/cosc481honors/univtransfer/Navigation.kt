package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object TwoYearInput

@Serializable
object CourseInputScreen

@Serializable
object SummaryPage

fun NavGraphBuilder.twoYearInputDestination(
    database: AppDatabase,
    onCollegeSelected: (College) -> Unit
) {
    composable<TwoYearInput> {
        SearchForm(
            database = database,
            searchType = SearchType.TWO_YEAR,
            onCollegeSelected = onCollegeSelected,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

fun NavGraphBuilder.courseInputScreen(
    navController: NavController,
    database: AppDatabase,
    twoYearCollege: () -> College?,
    onCourseListSelected: (List<Course>) -> Unit
) {
    composable<CourseInputScreen> {
        CourseInputForm(
            navController = navController,
            database = database,
            college = twoYearCollege(),
            onCourseListSelected = onCourseListSelected,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

fun NavGraphBuilder.summaryPage(
    navController: NavController,
    database: AppDatabase,
    courseList: () -> List<Course>
) {
    composable<SummaryPage> {
        SummaryPage(
            navController = navController,
            database = database,
            courseList = courseList(),
        )
    }
}