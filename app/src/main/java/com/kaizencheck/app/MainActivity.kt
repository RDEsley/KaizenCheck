package com.kaizencheck.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kaizencheck.app.data.ChecklistDatabase
import com.kaizencheck.app.ui.screens.ItemsScreen
import com.kaizencheck.app.ui.screens.ListsScreen
import com.kaizencheck.app.ui.theme.KaizenCheckTheme
import com.kaizencheck.app.viewmodel.ChecklistViewModel

class MainActivity : ComponentActivity() {

    private val database by lazy { ChecklistDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KaizenCheckTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    KaizenCheckNavHost(database = database)
                }
            }
        }
    }
}

@Composable
fun KaizenCheckNavHost(database: ChecklistDatabase) {
    val navController = rememberNavController()
    val viewModel: ChecklistViewModel = viewModel(
        factory = ChecklistViewModel.factory(database)
    )

    NavHost(
        navController = navController,
        startDestination = "lists"
    ) {
        composable("lists") {
            ListsScreen(
                viewModel = viewModel,
                onListClick = { id ->
                    navController.navigate("items/$id")
                }
            )
        }

        composable(
            route = "items/{checklistId}",
            arguments = listOf(navArgument("checklistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val checklistId = backStackEntry.arguments?.getLong("checklistId") ?: 0L

            ItemsScreen(
                checklistId = checklistId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
