package com.project.stocksage.presentation.bottom_navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.stocksage.presentation.company_info.CompanyInfoScreen
import com.project.stocksage.presentation.company_listing.CompanyListingScreen
import com.project.stocksage.presentation.news.NewsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StockSageBottomAppBar() {
    val navigationController = rememberNavController()
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.AutoMirrored.Filled.List)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Green
            ) {
                IconButton(
                    onClick = {
                        selected.value = Icons.AutoMirrored.Filled.List
                        navigationController.navigate(Screens.CompanyListing) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.List,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = if (selected.value == Icons.AutoMirrored.Filled.List) {
                            Color.Black
                        } else {
                            Color.Gray
                        }
                    )
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Newspaper
                        navigationController.navigate(Screens.NewsScreen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Newspaper,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = if (selected.value == Icons.Default.Newspaper) {
                            Color.Black
                        } else {
                            Color.Gray
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navigationController,
            startDestination = Screens.CompanyListing,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Screens.CompanyListing> { CompanyListingScreen(navController = navigationController) }
            composable<Screens.NewsScreen> { NewsScreen() }
            composable(
                route = Screens.CompanyInfo.screen,
                arguments = listOf(navArgument("symbol") { type = NavType.StringType })
            ) { backStackEntry ->
                val symbol = backStackEntry.arguments?.getString("symbol")
                symbol?.let {
                    CompanyInfoScreen(symbol = it)

                }
            }
        }
    }
}