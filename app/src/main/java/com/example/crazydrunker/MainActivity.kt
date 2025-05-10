package com.example.crazydrunker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.crazydrunker.screens.FAP
import com.example.crazydrunker.screens.JuiceBlock
import com.example.crazydrunker.screens.ProductScreen
import com.example.crazydrunker.screens.Timer
import com.example.crazydrunker.screens.TopBar
import com.example.crazydrunker.ui.theme.CrazyDrunkerTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CrazyDrunkerTheme {
                val navController = rememberNavController()
                ScreenSize.getInstance().Initialize(activity = this)

                NavHost(
                    navController = navController,
                    startDestination = HomeScreenData
                ) {
                    composable<HomeScreenData> { navBackStackEntry ->

                        // Save only id... Not full struct.
                        var selectedJuiceId by rememberSaveable { mutableStateOf<String?>(null) }

                        // Find corresponding juice by id
                        val selectedJuice = selectedJuiceId?.let {
                            id-> CocktailsData.getCocktail(id)
                        }

                        Scaffold(
                            topBar = { TopBar("Crazy Drunker", showBackButton = false) },
                        ) { innerPadding ->
                            Box(modifier = Modifier.padding(innerPadding)) {
                                JuiceBlock(
                                    juices = CocktailsData.cocktailList,
                                    navController = navController,
                                    selectedJuice = selectedJuice,
                                    onJuiceSelected = { juice ->
                                        val windowSize = ScreenSize.getInstance().getDeviceSize()
                                        if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                                            navController.navigate(juice)
                                        } else {
                                            selectedJuiceId = juice.id
                                        }
                                    }
                                )
                            }
                        }
                    }
                    composable<DetailsScreenData> {
                        var arg = it.toRoute<DetailsScreenData>()
                        Scaffold(
                            topBar = {
                                TopBar(
                                    "",
                                    showBackButton = true,
                                    onBackClick = { navController.navigate(HomeScreenData) }
                                )
                            },
                            floatingActionButton = {
                                FAP(
                                    onTimerClick = { navController.navigate(TimerScreenData(arg.id)) },
                                )
                            }
                        )
                        {
                            Box(modifier = Modifier.padding(it)) {
                                ProductScreen(arg, navController = navController)
                            }
                        }
                    }
                    composable<TimerScreenData> { it ->
                        val timerParameters = it.toRoute<TimerScreenData>()
                        CrazyDrunkerTheme {
                            Scaffold(
                                topBar = {
                                    TopBar(
                                        "",
                                        showBackButton = true,
                                        onBackClick = {
                                            navController.popBackStack()
                                        }
                                    )
                                }
                            ) {
                                Box(modifier = Modifier.padding(it)) {
                                    Timer(timerParameters,navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}