package com.raj.tngpractice.feature.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.tngpractice.feature.general.model.Resource
import com.raj.tngpractice.feature.user.data.repository.UserRepository
import com.raj.tngpractice.feature.user.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userUIStateChannel = Channel<UsersUiState>()
    val userUiState = _userUIStateChannel.receiveAsFlow()
    private var totalUserList = emptyList<User>()
    private val companyList = mutableSetOf<String>()
    private var isCurrentlyInAscendingOrder = true

    fun onUserUIEvent(uiEvent: UsersUiEvent) {
        when(uiEvent) {
            is UsersUiEvent.RequestUserData -> fetchUserData()
            is UsersUiEvent.ProcessCompanySelection -> processCompanySelection(uiEvent.companyName)
            is UsersUiEvent.SortUser -> sortUserList()
        }
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            _userUIStateChannel.send(UsersUiState.UpdateLoadingProgress(true))
            when(val response = userRepository.fetchAllUsers()) {
                is Resource.Success -> {
                    totalUserList = response.data as List<User>
                    totalUserList.forEach { user -> companyList.add(user.company.name) }
                    _userUIStateChannel.send(UsersUiState.UpdatePageUI(companyList, totalUserList))
                }
                else -> _userUIStateChannel.send(UsersUiState.ShowErrorMessage(response.message!!))
            }
            _userUIStateChannel.send(UsersUiState.UpdateLoadingProgress(false))
        }
    }

    private fun processCompanySelection(selectedCompanyName : String) {
        viewModelScope.launch {
            _userUIStateChannel.send(UsersUiState.UpdateLoadingProgress(true))
            if (selectedCompanyName.isEmpty()) {
                _userUIStateChannel.send(UsersUiState.UpdateUserList(totalUserList))
            } else {
                val filteredList = totalUserList.filter { it.company.name == selectedCompanyName }
                _userUIStateChannel.send(UsersUiState.UpdateUserList(filteredList))
            }
            _userUIStateChannel.send(UsersUiState.UpdateLoadingProgress(false))
        }
    }

    private fun sortUserList() {
        viewModelScope.launch {
            _userUIStateChannel.send(UsersUiState.UpdateLoadingProgress(true))
            if (isCurrentlyInAscendingOrder) {
                isCurrentlyInAscendingOrder = false
                _userUIStateChannel.send(UsersUiState.UpdateUserList(totalUserList.sortedByDescending { it.name }))
            } else {
                isCurrentlyInAscendingOrder = true
                _userUIStateChannel.send(UsersUiState.UpdateUserList(totalUserList.sortedBy { it.name }))
            }
            _userUIStateChannel.send(UsersUiState.UpdateLoadingProgress(false))
        }
    }

}

sealed class UsersUiState {
    data class UpdateLoadingProgress(val shouldShow : Boolean) : UsersUiState()
    data class ShowErrorMessage(val message : String) : UsersUiState()
    data class UpdatePageUI(val companyList : Set<String>, val users : List<User>) : UsersUiState()
    data class UpdateUserList(val users : List<User>) : UsersUiState()
}