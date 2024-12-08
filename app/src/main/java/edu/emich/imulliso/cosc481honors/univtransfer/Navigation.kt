package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object TwoYearInput
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

@Serializable
object FourYearInput

fun NavGraphBuilder.fourYearInputDestination(
    database: AppDatabase,
    onCollegeSelected: (College) -> Unit
) {
    composable<FourYearInput> {
        SearchForm(
            database = database,
            searchType = SearchType.FOUR_YEAR,
            onCollegeSelected = onCollegeSelected,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}


@Serializable
object CourseInputScreen
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

@Serializable
object SummaryPage
fun NavGraphBuilder.summaryPage(
    navController: NavController,
    database: AppDatabase,
    courseList: () -> List<Course>,
    viewTransferDestinationDetails: (College) -> Unit
) {
    composable<SummaryPage> {
        SummaryPage(
            navController = navController,
            database = database,
            courseList = courseList(),
            onTransferDestinationClicked = viewTransferDestinationDetails,
        )
    }
}


@Serializable
object EquivDetailsPage

fun NavGraphBuilder.equivDetailsPage(
    navController: NavController,
    database: AppDatabase,
    transferDestination: () -> College?,
    courseList: () -> List<Course>
) {
    composable<EquivDetailsPage> {
        EquivDetailsPage(
            navController = navController,
            database = database,
            transferDestination = transferDestination(),
            courseList = courseList(),
        )
    }
}