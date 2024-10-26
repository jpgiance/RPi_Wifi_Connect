package com.jorgegiance.rpiwificonnect.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Routes{

    @Serializable
    data object ScanListRoute: Routes
    @Serializable
    data object ConnectionDetailsRoute: Routes

}

enum class Destinations(
    val route: Routes,
){
    SCAN_LIST_ROUTE(
        route = Routes.ScanListRoute
    ),
    CONNECTION_DETAILS_ROUTE(
        route = Routes.ConnectionDetailsRoute
    ),

}

