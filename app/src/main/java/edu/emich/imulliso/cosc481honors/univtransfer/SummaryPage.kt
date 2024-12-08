package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    // key: college id, value: number of xferring courses
    var collegesWithEquiv by remember { mutableStateOf<Map<Int, Int>?>(null) }


    LaunchedEffect(courseList) {
        scope.launch {
            val map = emptyMap<Int, Int>().toMutableMap()
            courseList.forEach { course ->
                val equivList = database.transferEquivDao().getCollegeIdsWithEquiv(course.courseId)
                equivList.forEach { college ->
                    // count the equiv
                    map.putIfAbsent(college, 0)
                    map[college] =
                        map[college]!! + 1// it's safe doing nonnull assert bc putIfAbsent
                }
            }
            collegesWithEquiv = map.toMap()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.transfer_search_results)) },
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
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(collegesWithEquiv?.toString() ?: "Loading...")
        }
    }
}