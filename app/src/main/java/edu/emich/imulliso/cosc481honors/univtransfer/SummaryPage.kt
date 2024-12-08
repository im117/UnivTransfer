package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryPage(
    navController: NavController,
    database: AppDatabase,
    courseList: List<Course>,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    // we want the percentage of courses that transfer to each uni. bc of this, it makes sense
    // to keep track of *how many* courses transfer to each one
    // key: college, value: set of ids of courses that transfer
    var collegesWithEquiv by remember { mutableStateOf<Map<College, Set<Int>>?>(null) }

    LaunchedEffect(courseList) {
        scope.launch {
            val map = emptyMap<College, MutableSet<Int>>().toMutableMap()
            courseList.forEach { course ->
                val equivList = database.transferEquivDao().getCollegeIdsWithEquiv(course.courseId)

                // If equivalences exist, count the *course* for each college it transfers to
                equivList.forEach { collegeId ->
                    // Lookup college for the ID
                    val college = database.collegeDao().getCollegeById(collegeId)

                    // count the equiv
                    map.putIfAbsent(college, mutableSetOf())
                    map[college]!!.add(course.courseId)

                }
            }
            collegesWithEquiv = map.toMap()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.transfer_search_results)) },
                navigationIcon = {
                    // Back button
                    IconButton(onClick = {
                        // Go back
                        navController.popBackStack()
                    }) {
                        // Icon for back button
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_up)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search),
                        )
                    }
                })
        }, modifier = modifier
    ) { innerPadding ->
        if (collegesWithEquiv == null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(modifier = Modifier.padding(innerPadding)) {
                collegesWithEquiv!!.forEach { (college, courseIds) ->
                    ListItem(
                        headlineContent = { Text(college.collegeName) },
                        supportingContent = { Text("${courseIds.size * 100 / courseList.size}% of your courses have transfer equivalencies at this school") },
                        trailingContent = {
                            Icon(
                                Icons.AutoMirrored.Default.KeyboardArrowRight,
                                null
                            )
                        }
                    )
                }
            }
        }
    }
}