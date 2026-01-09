package com.rick.awtmenuapp.domain.model

data class MenuScreenData(
    val userProfile: UserProfile,
    val menus: List<MenuOption>
)

data class UserProfile(
    val name: String,
    val avatarUrl: String,
    val messageCount: Long,
    val notificationCount: Long
)

data class MenuOption(
    val title: String,
    val iconUrl: String?,
    val type: Int
)

