package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseInputForm(
    navController: NavController,
    database: AppDatabase,
    college: College?,
    onCourseListSelected: (List<Course>) -> Unit,
    viewModel: CourseInputViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = stringResource(R.string.course_list_empty)
    val scope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                // back icon
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // Go back
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            stringResource(R.string.navigate_up)
                        )
                    }
                },
                title = { Text(stringResource(R.string.enter_courses)) },

                )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (viewModel.courses.isEmpty()) {
                        // If the course list is empty, show an error snackbar
                        scope.launch {
                            snackbarHostState.showSnackbar(snackbarMessage)
                        }
                    } else {
                        onCourseListSelected(viewModel.courses)
                    }
                },
                icon = { Icon(Icons.Default.Check, null) },
                text = { Text(stringResource(R.string.done)) },
            )
        },
        modifier = modifier
    ) { innerPadding ->
        // Null handling
        if (college == null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(innerPadding)
            ) {
                Text(stringResource(R.string.error_please_select_a_college))
            }
            return@Scaffold
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
        ) {
            Text(
                stringResource(
                    R.string.enter_the_courses_you_have_taken_at_college,
                    college.collegeName
                ),
                modifier = Modifier.padding(8.dp)
            )
            // List the courses
            viewModel.courses.forEach { course ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${course.subjectCode} ${course.courseNumber}",
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                            .fillMaxSize()

                    )
                    IconButton(onClick = {
                        viewModel.removeCourse(course)
                    }) {
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
                if (course != null && course !in viewModel.courses) {
                    viewModel.addCourse(course)
                }
            })
        }
    }


}