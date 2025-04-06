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

package com.sigtrack.lorascan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sigtrack.lorascan.DataPacket
import com.sigtrack.lorascan.android.Logging
import com.sigtrack.lorascan.model.Node
import com.sigtrack.lorascan.model.UIViewModel
import com.sigtrack.lorascan.navigation.navigateToNavGraph
import com.sigtrack.lorascan.ui.components.NodeFilterTextField
import com.sigtrack.lorascan.ui.components.NodeMenuAction
import com.sigtrack.lorascan.ui.components.rememberTimeTickWithLifecycle
import com.sigtrack.lorascan.ui.message.navigateToMessages
import com.sigtrack.lorascan.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : ScreenFragment("Users"), Logging {

    private val model: UIViewModel by activityViewModels()

    private fun navigateToMessages(node: Node) = node.user.let { user ->
        val hasPKC = model.ourNodeInfo.value?.hasPKC == true && node.hasPKC // TODO use meta.hasPKC
        val channel = if (hasPKC) DataPacket.PKC_CHANNEL_INDEX else node.channel
        val contactKey = "$channel${user.id}"
        info("calling MessagesFragment filter: $contactKey")
        parentFragmentManager.navigateToMessages(contactKey)
    }

    private fun navigateToNodeDetails(nodeNum: Int) {
        info("calling NodeDetails --> destNum: $nodeNum")
        parentFragmentManager.navigateToNavGraph(nodeNum, "NodeDetails")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    NodesScreen(
                        model = model,
                        navigateToMessages = ::navigateToMessages,
                        navigateToNodeDetails = ::navigateToNodeDetails,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("LongMethod")
fun NodesScreen(
    model: UIViewModel = hiltViewModel(),
    navigateToMessages: (Node) -> Unit,
    navigateToNodeDetails: (Int) -> Unit,
) {
    val state by model.nodesUiState.collectAsStateWithLifecycle()

    val nodes by model.nodeList.collectAsStateWithLifecycle()
    val ourNode by model.ourNodeInfo.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    val currentTimeMillis = rememberTimeTickWithLifecycle()
    val connectionState by model.connectionState.collectAsStateWithLifecycle()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
    ) {
        stickyHeader {
            NodeFilterTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
                    .padding(8.dp),
                filterText = state.filter,
                onTextChange = model::setNodeFilterText,
                currentSortOption = state.sort,
                onSortSelect = model::setSortOption,
                includeUnknown = state.includeUnknown,
                onToggleIncludeUnknown = model::toggleIncludeUnknown,
                showDetails = state.showDetails,
                onToggleShowDetails = model::toggleShowDetails,
            )
        }

        items(nodes, key = { it.num }) { node ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                elevation = 6.dp,
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                NodeItem(
                    modifier = Modifier.animateContentSize(),
                    thisNode = ourNode,
                    thatNode = node,
                    gpsFormat = state.gpsFormat,
                    distanceUnits = state.distanceUnits,
                    tempInFahrenheit = state.tempInFahrenheit,
                    onAction = { menuItem ->
                        when (menuItem) {
                            is NodeMenuAction.Remove -> model.removeNode(node.num)
                            is NodeMenuAction.Ignore -> model.ignoreNode(node)
                            is NodeMenuAction.Favorite -> model.favoriteNode(node)
                            is NodeMenuAction.DirectMessage -> navigateToMessages(node)
                            is NodeMenuAction.RequestUserInfo -> model.requestUserInfo(node.num)
                            is NodeMenuAction.RequestPosition -> model.requestPosition(node.num)
                            is NodeMenuAction.TraceRoute -> model.requestTraceroute(node.num)
                            is NodeMenuAction.MoreDetails -> navigateToNodeDetails(node.num)
                        }
                    },
                    expanded = false,
                    currentTimeMillis = currentTimeMillis,
                    isConnected = connectionState.isConnected(),
                )
            }
        }
    }
}
