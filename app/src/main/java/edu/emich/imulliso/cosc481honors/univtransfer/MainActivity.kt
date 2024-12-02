package edu.emich.imulliso.cosc481honors.univtransfer

import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.emich.imulliso.cosc481honors.univtransfer.ui.theme.UnivTransferTheme
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
        FloatingActionButton(onClick = {
            navController.navigate(route = Search)
        }) {
            Icon(Icons.Outlined.Search, stringResource(R.string.new_search))

        }
    }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Home) {
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

@ExperimentalMaterial3Api
@Composable
fun SearchScreen(database: AppDatabase, modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    var lastUpdateTime by remember { mutableStateOf(-1L) }
    val collegeDao = remember {
        database.collegeDao()
    }
    var collegeSuggestions by remember { mutableStateOf(emptyList<College>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(query) {
        val currentTime: Long = Calendar.getInstance().time.time
        if (currentTime - lastUpdateTime > 5000) {
            scope.launch {
                collegeSuggestions = collegeDao.searchTop5(query)
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
                placeholder = { Text(stringResource(R.string.enter_4_year_college_name)) }
            ) {
                Column(Modifier.verticalScroll(rememberScrollState())) {


                    collegeSuggestions.forEach { college ->
                        ListItem(
                            headlineContent = { Text(college.collegeName) }
                        )
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



