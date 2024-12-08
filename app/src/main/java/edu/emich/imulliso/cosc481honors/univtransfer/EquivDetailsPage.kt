package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquivDetailsPage(
    navController: NavController,
    database: AppDatabase,
    transferDestination: College?,
    courseList: List<Course>,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    // we want the percentage of courses that transfer to each uni. bc of this, it makes sense
    // to keep track of *how many* courses transfer to each one
    // key: college, value: set of ids of courses that transfer
    var fourYearEquivMap by remember { mutableStateOf<Map<Course, Set<Course>>?>(null) }


    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    transferDestination?.collegeName ?: stringResource(R.string.error)
                )
            },
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
                }
            )
        }, modifier = modifier
    ) { innerPadding ->

        if (transferDestination == null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Text(stringResource(R.string.error_transfer_destination_must_be_specified))
            }

            return@Scaffold // do not continue with the rest of composition
        }
        LaunchedEffect(courseList) {
            scope.launch {
                val map = emptyMap<Course, MutableSet<Course>>().toMutableMap()
                courseList.forEach { twoYearCourse ->
                    val equivList = database.transferEquivDao().getEquivalentCoursesInCollege(
                        twoYearCourse.courseId,
                        transferDestination.collegeId
                    )

                    // If equivalent courses exist, record them
                    equivList.forEach { fourYearCourse ->
                        map.putIfAbsent(twoYearCourse, mutableSetOf())
                        map[twoYearCourse]!!.add(fourYearCourse)
                    }
                }
                fourYearEquivMap = map.toMap()
            }
        }

        if (fourYearEquivMap == null) {
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
                fourYearEquivMap!!.forEach { (twoYearCourse, fourYearList) ->

                    ListItem(

                        headlineContent = {
                            Text(
                                stringResource(
                                    R.string.full_course_listing,
                                    twoYearCourse.subjectCode,
                                    twoYearCourse.courseNumber,
                                    twoYearCourse.courseName
                                ),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Left,
                            )
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceTint)
                    )
                    fourYearList.forEach { fourYearCourse ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    stringResource(
                                        R.string.full_course_listing,
                                        fourYearCourse.subjectCode,
                                        fourYearCourse.courseNumber,
                                        fourYearCourse.courseName
                                    ),
                                )
                            },
                        )
                    }

                }
            }
        }
    }
}