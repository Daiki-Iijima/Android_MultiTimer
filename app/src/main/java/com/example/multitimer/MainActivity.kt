package com.example.multitimer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.multitimer.ui.MultiTimerList
import com.example.multitimer.ui.MultiTimerViewModel
import com.example.multitimer.ui.TimerDetail
import com.example.multitimer.ui.theme.MultiTimerTheme
import kotlinx.coroutines.delay

enum class ScreenType{
    MultiTimerList,
    TimerDetail,
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultiTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MultiTimerApp()
                }
            }
        }
    }
}


@Composable
fun MultiTimerApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
){
    val multiTimerViewModel: MultiTimerViewModel = viewModel()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination =currentBackStackEntry?.destination?.route

    MultiTimerTheme {
        Scaffold(
            floatingActionButton = {
                if( currentDestination != ScreenType.TimerDetail.name) {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(route = ScreenType.TimerDetail.name)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                }
            },
            modifier = modifier
        ) { innerPadding->

            NavHost(
                navController = navController,
                startDestination = ScreenType.MultiTimerList.name,
                modifier = Modifier.padding(innerPadding)){

                composable(route = ScreenType.MultiTimerList.name){
                    MultiTimerList(
                        timerListViewModel = multiTimerViewModel,
                    )
                }
                composable(route=ScreenType.TimerDetail.name){
                    TimerDetail {
                        navController.popBackStack()
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoroutineTestAppPreview() {
    MultiTimerApp()
}
