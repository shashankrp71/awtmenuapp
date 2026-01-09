package com.rick.awtmenuapp.domain.usecase

import com.rick.awtmenuapp.domain.model.MenuScreenData
import com.rick.awtmenuapp.domain.repository.NavigationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNavigationDataUseCase @Inject constructor(
    private val repo: NavigationRepository
) {
    operator fun invoke(): Flow<Result<MenuScreenData>> = repo.getMenuData()
}