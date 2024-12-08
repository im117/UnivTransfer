package edu.emich.imulliso.cosc481honors.univtransfer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun CourseInputForm(database: AppDatabase, college: College?, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    val courses = remember { mutableStateListOf<Course>() }

    Scaffold(modifier = modifier) { innerPadding ->
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
                .verticalScroll(state = rememberScrollState())
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
                    IconButton(onClick = {
                        courses.remove(course)
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
                if (course != null && course !in courses) {
                    courses.add(course)
                }
            })
        }
    }


}