/*
 * Copyright (c) 2025 Meshtastic LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sigtrack.lorascan.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sigtrack.lorascan.model.MetricsViewModel
import com.sigtrack.lorascan.ui.NodeDetailScreen
import com.sigtrack.lorascan.ui.components.DeviceMetricsScreen
import com.sigtrack.lorascan.ui.components.EnvironmentMetricsScreen
import com.sigtrack.lorascan.ui.components.NodeMapScreen
import com.sigtrack.lorascan.ui.components.PositionLogScreen
import com.sigtrack.lorascan.ui.components.PowerMetricsScreen
import com.sigtrack.lorascan.ui.components.SignalMetricsScreen
import com.sigtrack.lorascan.ui.components.TracerouteLogScreen

fun NavGraphBuilder.addNodDetailSection(navController: NavController) {
    composable<Route.NodeDetail> {
        NodeDetailScreen(
            onNavigate = navController::navigate,
        )
    }
    composable<Route.DeviceMetrics> {
        val parentEntry = remember { navController.getBackStackEntry<Route.NodeDetail>() }
        DeviceMetricsScreen(
            viewModel = hiltViewModel<MetricsViewModel>(parentEntry),
        )
    }
    composable<Route.NodeMap> {
        val parentEntry = remember { navController.getBackStackEntry<Route.NodeDetail>() }
        NodeMapScreen(
            viewModel = hiltViewModel<MetricsViewModel>(parentEntry),
        )
    }
    composable<Route.PositionLog> {
        val parentEntry = remember { navController.getBackStackEntry<Route.NodeDetail>() }
        PositionLogScreen(
            viewModel = hiltViewModel<MetricsViewModel>(parentEntry),
        )
    }
    composable<Route.EnvironmentMetrics> {
        val parentEntry = remember { navController.getBackStackEntry<Route.NodeDetail>() }
        EnvironmentMetricsScreen(
            viewModel = hiltViewModel<MetricsViewModel>(parentEntry),
        )
    }
    composable<Route.SignalMetrics> {
        val parentEntry = remember { navController.getBackStackEntry<Route.NodeDetail>() }
        SignalMetricsScreen(
            viewModel = hiltViewModel<MetricsViewModel>(parentEntry),
        )
    }
    composable<Route.PowerMetrics> {
        val parentEntry = remember { navController.getBackStackEntry<Route.NodeDetail>() }
        PowerMetricsScreen(
            viewModel = hiltViewModel<MetricsViewModel>(parentEntry),
        )
    }
    composable<Route.TracerouteLog> {
        val parentEntry = remember { navController.getBackStackEntry<Route.NodeDetail>() }
        TracerouteLogScreen(
            viewModel = hiltViewModel<MetricsViewModel>(parentEntry),
        )
    }
}
