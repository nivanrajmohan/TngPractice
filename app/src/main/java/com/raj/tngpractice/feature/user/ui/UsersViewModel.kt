package com.raj.tngpractice.feature.user.ui

import androidx.lifecycle.ViewModel
import com.raj.tngpractice.feature.user.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {


}