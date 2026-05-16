package com.kevinrabbe.quietlog.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kevinrabbe.quietlog.feature.games.GamesScreen
import com.kevinrabbe.quietlog.feature.home.HomeScreen
import com.kevinrabbe.quietlog.feature.reminders.RemindersScreen
import com.kevinrabbe.quietlog.feature.settings.SettingsScreen
import com.kevinrabbe.quietlog.feature.shopping.ShoppingScreen

import androidx.compose.ui.Modifier

/**
 * The central navigation graph for QuietLog.
 * Each destination maps to a placeholder screen for now.
 */
@Composable
fun QuietLogNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME,
        modifier = modifier
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen()
        }
        composable(NavRoutes.REMINDERS) {
            RemindersScreen()
        }
        composable(NavRoutes.SHOPPING) {
            ShoppingScreen()
        }
        composable(NavRoutes.GAMES) {
            GamesScreen()
        }
        composable(NavRoutes.SETTINGS) {
            SettingsScreen()
        }
    }
}
