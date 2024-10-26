package com.jorgegiance.rpiwificonnect.ui.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ScreenNavigator {

    private var scope = CoroutineScope(Dispatchers.Main.immediate)

    private lateinit var parentNavController:NavHostController

    private var parentNavControllerObserverJob: Job? = null

    val currentRoute = MutableStateFlow<Routes?>(null)

    fun setParentNavController(navController: NavHostController){
        parentNavController = navController


        parentNavControllerObserverJob?.cancel()
        parentNavControllerObserverJob = scope.launch {
            navController.currentBackStackEntryFlow.map { backStackEntry ->

                val matchingDestination = Destinations.entries.find { destination ->
                    backStackEntry.destination.hierarchy.any { it.hasRoute(destination.route::class) }
                }
                // Update the current route if a matching destination is found
                currentRoute.value = matchingDestination?.route

            }
        }
    }

    fun toRoute(route: Routes) {
        parentNavController.navigate(route)
    }

    fun navigateBack() {
        parentNavController.popBackStack()
    }
}