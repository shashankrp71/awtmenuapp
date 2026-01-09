package com.rick.awtmenuapp.domain.repository

import com.rick.awtmenuapp.domain.model.MenuScreenData
import kotlinx.coroutines.flow.Flow

interface NavigationRepository {
    fun getMenuData(): Flow<Result<MenuScreenData>>
}