package com.raj.tngpractice.unit.feature.user.ui

import app.cash.turbine.test
import com.raj.tngpractice.feature.general.model.Resource
import com.raj.tngpractice.feature.user.data.repository.UserRepository
import com.raj.tngpractice.feature.user.ui.UsersUiEvent
import com.raj.tngpractice.feature.user.ui.UsersViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.google.common.truth.Truth.assertThat
import com.raj.tngpractice.feature.user.model.CompanyItem
import com.raj.tngpractice.feature.user.model.User
import com.raj.tngpractice.feature.user.ui.UsersUiState
import io.mockk.coVerify

@RunWith(RobolectricTestRunner::class)
class UserViewModelTest {

    private lateinit var userRepository: UserRepository
    private lateinit var usersViewModel: UsersViewModel

    @Before
    fun setup() {
        userRepository = mockk<UserRepository>()
        usersViewModel = UsersViewModel(userRepository)
    }

    @Test
    fun fetchUserData_withAPIError_ShouldReturnError() = runTest {
        coEvery { userRepository.fetchAllUsers() } returns Resource.Error("Error", 0)
        usersViewModel.userUiState.test {
            usersViewModel.onUserUIEvent(UsersUiEvent.RequestUserData)
            assertThat(awaitItem()).isEqualTo(UsersUiState.UpdateLoadingProgress(true))
            coVerify(exactly = 1) { userRepository.fetchAllUsers() }
            assertThat(awaitItem()).isEqualTo(UsersUiState.ShowErrorMessage("Error"))
            assertThat(awaitItem()).isEqualTo(UsersUiState.UpdateLoadingProgress(false))
            expectNoEvents()
        }
    }

    @Test
    fun fetchUserData_withAPISuccess_ShouldUpdateUI() = runTest {
        val dummyCompany = CompanyItem(name = "Testing")
        val dummyList = listOf(User(company = dummyCompany))
        coEvery { userRepository.fetchAllUsers() } returns Resource.Success(dummyList)
        usersViewModel.userUiState.test {
            usersViewModel.onUserUIEvent(UsersUiEvent.RequestUserData)
            assertThat(awaitItem()).isEqualTo(UsersUiState.UpdateLoadingProgress(true))
            coVerify(exactly = 1) { userRepository.fetchAllUsers() }
            assertThat(awaitItem()).isEqualTo(UsersUiState.UpdatePageUI(setOf(dummyCompany.name), dummyList))
            assertThat(awaitItem()).isEqualTo(UsersUiState.UpdateLoadingProgress(false))
            expectNoEvents()
        }
    }

    /**
    @Test
    fun processCompanySelection_withSpecificCompanyName_shouldFilter() = runTest {
        val firstCompany = CompanyItem(name = "Testing")
        val secondCompany = CompanyItem(name = "Testing2")
        val dummyList = listOf(User(company = firstCompany), User(company = secondCompany))
        usersViewModel.userUiState.test {
            usersViewModel.onUserUIEvent(UsersUiEvent.ProcessCompanySelection(firstCompany.name))
            assertThat(awaitItem()).isEqualTo(UsersUiState.UpdateLoadingProgress(true))
            assertThat(awaitItem()).isEqualTo(UsersUiState.UpdateUserList(listOf(User(company = firstCompany))))
            assertThat(awaitItem()).isEqualTo(UsersUiState.UpdateLoadingProgress(false))
            expectNoEvents()
        }
    }
    **/
}