package com.rick.awtmenuapp.data.remote.repository

import com.rick.awtmenuapp.data.remote.ApiService
import com.rick.awtmenuapp.domain.model.MenuOption
import com.rick.awtmenuapp.domain.model.MenuScreenData
import com.rick.awtmenuapp.domain.model.UserProfile
import com.rick.awtmenuapp.domain.repository.NavigationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NavigationRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): NavigationRepository {
    override fun getMenuData(): Flow<Result<MenuScreenData>> = flow {
        try {
            val response = apiService.getNavigationDetails()

            val user = UserProfile(
                name = response.resultDto?.title ?: "Guest",
                avatarUrl = response.resultDto?.userPhoto ?: "",
                messageCount = response.resultDto?.messageCount ?: 0,
                notificationCount = response.resultDto?.notificationCount ?: 0
            )

            val allItems = response.resultDto?.menus?.map{
                MenuOption(
                    title = it.label ?: "Guest",
                    iconUrl = it.icon,
                    type = it.type ?: 0
                )
            } ?: emptyList()

            val data = MenuScreenData(
                userProfile = user,
                menus = allItems
            )

            emit(Result.success(data))

        }catch (e: Exception){
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }
}