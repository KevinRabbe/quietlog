package com.kevinrabbe.quietlog.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines each bottom navigation destination with its route, label and icon.
 */
enum class BottomNavDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    HOME(
        route = NavRoutes.HOME,
        label = "Home",
        icon = Icons.Filled.Home
    ),
    REMINDERS(
        route = NavRoutes.REMINDERS,
        label = "Reminders",
        icon = Icons.Filled.CalendarMonth
    ),
    SHOPPING(
        route = NavRoutes.SHOPPING,
        label = "Shopping",
        icon = Icons.Filled.ShoppingCart
    ),
    GAMES(
        route = NavRoutes.GAMES,
        label = "Games",
        icon = Icons.Filled.SportsEsports
    ),
    SETTINGS(
        route = NavRoutes.SETTINGS,
        label = "Settings",
        icon = Icons.Filled.Settings
    )
}
