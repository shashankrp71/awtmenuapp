package com.rick.awtmenuapp.presentation.menu

import androidx.compose.runtime.Composable
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rick.awtmenuapp.domain.model.MenuScreenData
import com.rick.awtmenuapp.domain.model.UserProfile
import com.rick.awtmenuapp.data.remote.model.MenuItemDto
import com.rick.awtmenuapp.domain.model.MenuOption


@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val isAppsExpanded by viewModel.isAppExpanded.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF5F6FA),
        bottomBar = { SignOutButton() }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val ui = state) {
                is MenuUiState.Loading -> Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is MenuUiState.Error -> Box(Modifier.fillMaxSize()) {
                    Text("Error: ${ui.message}", Modifier.align(Alignment.Center))
                }
                is MenuUiState.Success -> MenuContent(
                    data = ui.data,
                    isExpanded = isAppsExpanded,
                    onToggleExpand = { viewModel.toggleAppExpansion() }
                )
            }
        }
    }
}

@Composable
fun MenuContent(
    data: MenuScreenData,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    val sections = remember(data.menus) {
        val groups = mutableListOf<Pair<String, List<MenuOption>>>()
        var currentHeader = ""
        var currentItems = mutableListOf<MenuOption>()

        data.menus.forEach { item ->
            if (item.type == 0) {
                if (currentItems.isNotEmpty()) groups.add(currentHeader to currentItems)
                currentHeader = item.title
                currentItems = mutableListOf()
            } else {
                currentItems.add(item)
            }
        }
        if (currentItems.isNotEmpty()) groups.add(currentHeader to currentItems)
        groups
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        /**
         * Header & Profile Section
         * */
        item(span = { GridItemSpan(2) }) { HeaderSection() }
        item(span = { GridItemSpan(2) }) { ProfileSection(data.userProfile) }

        /**
         * Quick Actions (Message & Notifications)
         * */
        item(span = { GridItemSpan(2) }) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Message",
                    count = data.userProfile.messageCount,
                    iconRes = android.R.drawable.ic_dialog_email
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Notifications",
                    count = data.userProfile.notificationCount,
                    iconRes = android.R.drawable.ic_popup_reminder
                )
            }
        }

        /**  Sections */
        sections.forEach { (header, items) ->
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = header,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            /** APPS*/
            val isAppsSection = header.equals("APPS", ignoreCase = true)
            val displayItems = if (isAppsSection && !isExpanded) items.take(4) else items

            items(displayItems) { item ->
                MenuGridItem(item)
            }

            /** "See More" */
            if (isAppsSection && items.size > 4) {
                item(span = { GridItemSpan(2) }) {
                    Button(
                        onClick = onToggleExpand,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    ) {
                        Text(text = if (isExpanded) "See Less" else "See More", color = Color.Black)
                    }
                }
            }
        }

        /** Rate Us Section */
        item(span = { GridItemSpan(2) }) {
            CenteredActionRow(icon = android.R.drawable.star_on, text = "Rate Us")
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Menu", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(android.R.drawable.ic_menu_mapmode), null, Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("IND-INR-EN", fontSize = 12.sp)
                }
            }
            IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") }
        }
    }
}

@Composable
fun ProfileSection(user: UserProfile) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = "Profile",
                modifier = Modifier.size(50.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(user.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.weight(1f))
            Text("Edit Profile", color = Color(0xFF2196F3), fontSize = 14.sp, modifier = Modifier.clickable { })
        }
    }
}

@Composable
fun MenuGridItem(item: MenuOption) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.iconUrl,
                contentDescription = item.title,
                modifier = Modifier.size(24.dp),
                placeholder = painterResource(android.R.drawable.ic_menu_gallery)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 1)
        }
    }
}

@Composable
fun QuickActionCard(modifier: Modifier, title: String, count: Long, iconRes: Int) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(Modifier.fillMaxWidth()) {
                Icon(painterResource(iconRes), null, tint = Color(0xFF2196F3))
                if (count > 0) {
                    Surface(Modifier.align(Alignment.TopEnd), color = Color.Red, shape = CircleShape) {
                        Text(count.toString(), color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 4.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CenteredActionRow(icon: Int, text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Icon(painterResource(icon), null, tint = Color.Red, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Composable
fun SignOutButton() {
    Button(
        onClick = { },
        modifier = Modifier.fillMaxWidth().padding(16.dp).border(1.dp, Color.Red, RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Text("Sign Out", color = Color.Red)
    }
}