package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object TwoYearInput

@Serializable
object CourseInputScreen

@Serializable
object FourYear

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

fun NavGraphBuilder.courseInputScreen(database: AppDatabase, twoYearCollege: () -> College?) {
    composable<CourseInputScreen> {
        CourseInputForm(
            database = database,
            college = twoYearCollege(),
            modifier = Modifier
                .padding(32.dp)
                .fillMaxSize()
        )
    }
}