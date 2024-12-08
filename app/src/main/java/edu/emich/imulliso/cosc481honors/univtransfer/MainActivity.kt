package edu.emich.imulliso.cosc481honors.univtransfer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.emich.imulliso.cosc481honors.univtransfer.ui.theme.UnivTransferTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
fun App(database: AppDatabase) {
    val navController = rememberNavController()
    var twoYearCollege by remember { mutableStateOf<College?>(null) }
    var courseList by remember { mutableStateOf(emptyList<Course>()) }
    var transferDestination by remember { mutableStateOf<College?>(null) }

    NavHost(navController = navController, startDestination = TwoYearInput) {
        twoYearInputDestination(database, onCollegeSelected = { college ->
            twoYearCollege = college
            navController.navigate(CourseInputScreen)
        })



        courseInputScreen(
            navController = navController,
            database = database,
            twoYearCollege = { twoYearCollege },
            onCourseListSelected = { courses ->
                courseList = courses
                navController.navigate(SummaryPage)
            }
        )
        summaryPage(
            navController = navController,
            database = database,
            courseList = { courseList },
            viewTransferDestinationDetails = { college ->
                transferDestination = college
                navController.navigate(EquivDetailsPage)
            }
        )

        fourYearInputDestination(database, onCollegeSelected = { college ->
            transferDestination = college
            navController.navigate(EquivDetailsPage)
        })

        equivDetailsPage(
            navController = navController,
            database = database,
            transferDestination = { transferDestination },
            courseList = { courseList }
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