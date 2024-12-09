package edu.emich.imulliso.cosc481honors.univtransfer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.emich.imulliso.cosc481honors.univtransfer.ui.theme.UnivTransferTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val db = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "transfer-db"
        ).createFromAsset("transfer-db.db").build()
        setContent {
            UnivTransferTheme {
                App(database = db)
            }

        }
    }

}


@Composable
fun App(
    database: AppDatabase,
    viewModel: MainViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = TwoYearInput) {
        twoYearInputDestination(database, onCollegeSelected = { college ->
            viewModel.updateState(twoYearCollege = college)
            navController.navigate(CourseInputScreen)
        })



        courseInputScreen(
            navController = navController,
            database = database,
            twoYearCollege = { uiState.twoYearCollege },
            onCourseListSelected = { courses ->
                viewModel.updateState(courseList = courses)
                navController.navigate(SummaryPage)
            }
        )
        summaryPage(
            navController = navController,
            database = database,
            courseList = { uiState.courseList },
            viewTransferDestinationDetails = { college ->
                viewModel.updateState(transferDestination = college)
                navController.navigate(EquivDetailsPage)
            }
        )

        fourYearInputDestination(database, onCollegeSelected = { college ->
            viewModel.updateState(transferDestination = college)
            navController.navigate(EquivDetailsPage)
        })

        equivDetailsPage(
            navController = navController,
            database = database,
            transferDestination = { uiState.transferDestination },
            courseList = { uiState.courseList }
        )
    }
}


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center, modifier = modifier
    ) {
        Text("Press the search icon to start")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    HomeScreen(modifier = modifier.size(414.dp, 896.dp))
}