package com.rick.awtmenuapp.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rick.awtmenuapp.domain.model.MenuScreenData
import com.rick.awtmenuapp.domain.usecase.GetNavigationDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getNavigationDataUseCase: GetNavigationDataUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _isAppExpanded = MutableStateFlow(false)
    val isAppExpanded = _isAppExpanded.asStateFlow()

    init {
        loadData()
    }

    fun loadData(){
        viewModelScope.launch {
            _uiState.value = MenuUiState.Loading
            getNavigationDataUseCase().collect{ result ->
                result.onSuccess { data ->
                    _uiState.value = MenuUiState.Success(data)
                }
                result.onFailure { msg ->
                    _uiState.value = MenuUiState.Error(msg.message ?: "Something went wrong")
                }
            }
        }
    }

    fun toggleAppExpansion(){
        _isAppExpanded.value = !_isAppExpanded.value
    }
}

sealed class MenuUiState{
    object Loading: MenuUiState()
    data class Success(val data: MenuScreenData): MenuUiState()
    data class Error(val message: String): MenuUiState()
}