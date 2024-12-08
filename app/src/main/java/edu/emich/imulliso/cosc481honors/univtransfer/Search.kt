package edu.emich.imulliso.cosc481honors.univtransfer

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
    var searchFieldActive by remember { mutableStateOf(false) }
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            SearchBar(
                query = query,

                onQueryChange = { query = it },
                onSearch = {},
                active = searchFieldActive,
                onActiveChange = { searchFieldActive = it },
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
    var searchFieldActive by remember { mutableStateOf(false) }

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
                active = searchFieldActive,
                onActiveChange = { searchFieldActive = it }
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