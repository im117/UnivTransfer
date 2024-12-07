package edu.emich.imulliso.cosc481honors.univtransfer

import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.emich.imulliso.cosc481honors.univtransfer.ui.theme.UnivTransferTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "transfer-db"
        ).createFromAsset("transfer-db.db").build()
        setContent {
            UnivTransferTheme {
                App(database = db, modifier = Modifier.fillMaxSize())
            }

        }
    }

}


@Serializable
object Home

@Serializable
object Search


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(database: AppDatabase, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.app_name)) })
    }, floatingActionButton = {
        /*FloatingActionButton(onClick = {
            navController.navigate(route = Search)
        }) {
            Icon(Icons.Outlined.Search, stringResource(R.string.new_search))

        }*/
    }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Search) {
            composable<Home> {
                HomeScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
            composable<Search> {
                SearchScreen(
                    database = database,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CourseInputForm(database: AppDatabase, college: College, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    val courses = remember { mutableStateListOf<Course>() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(state = rememberScrollState())
    ) {
        // List the courses
        courses.forEach { course ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${course.subjectCode} ${course.courseNumber}",
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .fillMaxSize()

                )
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Clear, stringResource(R.string.delete))
                }
            }
        }

        Button(onClick = {
            showDialog = true
        }) {
            Icon(Icons.Default.Add, contentDescription = null)
            Text(stringResource(R.string.add_course))
        }
    }

    if (showDialog) {
        CourseAddDialog(database, college, onDismissRequest = { course ->
            showDialog = false
            if (course != null && course !in courses) {
                courses.add(course)
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseAddDialog(
    database: AppDatabase,
    collegeToSearch: College,
    onDismissRequest: (Course?) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var lastUpdateTime by remember { mutableLongStateOf(-1L) }
    var searchSuggestions by remember { mutableStateOf(emptyList<Course>()) }
    val scope = rememberCoroutineScope()
    val courseDao = remember {
        database.courseDao()
    }

    LaunchedEffect(query) {
        if (query == "") {
            searchSuggestions = emptyList()
            return@LaunchedEffect
        }

        scope.launch {
            delay(1000)
            searchSuggestions =
                courseDao.searchCoursesInCollege(query, collegeToSearch.collegeId)
            lastUpdateTime = Calendar.getInstance().time.time
        }


    }
    Dialog(onDismissRequest = { onDismissRequest(null) }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(32.dp)
        ) {
            SearchBar(
                query = query,

                placeholder = {
                    Text(stringResource(R.string.enter_course))
                },
                onQueryChange = { query = it },
                onSearch = {},
                active = true,
                onActiveChange = {}
            ) {
                searchSuggestions.forEach { course ->

                    Surface(
                        onClick = {
                            onDismissRequest(course)
                        }
                    ) {
                        ListItem(
                            headlineContent = { Text("${course.subjectCode} ${course.courseNumber}") },
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun SearchScreen(database: AppDatabase, modifier: Modifier = Modifier) {
    var step by remember { mutableIntStateOf(0) }
    var twoYearCollege by remember { mutableStateOf<College?>(null) }
    when (step) {
        0 -> SearchForm(
            database = database,
            searchType = SearchType.TWO_YEAR,
            onCollegeSelected = { college ->
                twoYearCollege = college
                step++
            },
            modifier = modifier
        )

        1 -> {
            if (twoYearCollege == null) {
                step = 0
            } else {
                CourseInputForm(
                    database = database,
                    college = twoYearCollege!!,
                    modifier = modifier
                )
            }
        }

        2 -> SearchForm(
            database = database,
            searchType = SearchType.FOUR_YEAR,
            onCollegeSelected = {},
            modifier = modifier
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchForm(
    database: AppDatabase,
    searchType: SearchType,
    onCollegeSelected: (College) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var lastUpdateTime by remember { mutableLongStateOf(-1L) }
    val collegeDao = remember {
        database.collegeDao()
    }
    var collegeSuggestions by remember { mutableStateOf(emptyList<College>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(query) {
        val currentTime: Long = Calendar.getInstance().time.time
        if (currentTime - lastUpdateTime > 5000) {
            scope.launch {
                collegeSuggestions = when (searchType) {
                    SearchType.TWO_YEAR -> collegeDao.searchTop5TwoYear(query)
                    SearchType.FOUR_YEAR -> collegeDao.searchTop5FourYear(query)
                }

                lastUpdateTime = Calendar.getInstance().time.time
            }

        }
    }

    Box(modifier = modifier) {
        Column {
            SearchBar(
                query = query,

                onQueryChange = { query = it },
                onSearch = {},
                active = true,
                onActiveChange = { },
                placeholder = {
                    Text(
                        stringResource(
                            when (searchType) {
                                SearchType.TWO_YEAR -> R.string.enter_2_year_college_name
                                SearchType.FOUR_YEAR -> R.string.enter_4_year_college_name
                            }

                        )
                    )
                }
            ) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    collegeSuggestions.forEach { college ->
                        Surface(
                            onClick = {
                                onCollegeSelected(college)
                            }
                        ) {
                            ListItem(
                                headlineContent = { Text(college.collegeName) },
                            )
                        }
                    }
                }
            }
        }
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



