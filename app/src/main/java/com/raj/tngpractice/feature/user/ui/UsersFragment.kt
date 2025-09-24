package com.raj.tngpractice.feature.user.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.raj.tngpractice.R
import com.raj.tngpractice.databinding.FragmentUsersBinding
import com.raj.tngpractice.feature.general.ui.LoadingDialog
import com.raj.tngpractice.feature.user.adapter.UserAdapter
import com.raj.tngpractice.feature.user.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UsersFragment : Fragment(), View.OnClickListener {

    private var _usersBinding : FragmentUsersBinding? = null
    private val usersBinding get() = _usersBinding!!
    private val viewModel by viewModels<UsersViewModel>()
    private lateinit var loading : LoadingDialog
    @Inject lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _usersBinding = FragmentUsersBinding.inflate(inflater, container, false)
        return usersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = LoadingDialog()
        viewModel.onUserUIEvent(UsersUiEvent.RequestUserData)
        usersBinding.apply {
            rvUser.adapter = userAdapter
            ivSort.setOnClickListener(this@UsersFragment)
            ivRefresh.setOnClickListener(this@UsersFragment)
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userUiState.collect {
                    when(it) {
                        is UsersUiState.UpdateLoadingProgress -> if (it.shouldShow) loading.show(childFragmentManager, "") else loading.dismiss()
                        is UsersUiState.ShowErrorMessage -> showErrorMessage(it.message)
                        is UsersUiState.UpdatePageUI -> updatePageUI(it.companyList.toList(), it.users)
                        is UsersUiState.UpdateUserList -> updateUserList(it.users)
                    }
                }
            }
        }
    }

    /* just show in same textview */
    private fun showErrorMessage(errorMessage : String) {
        usersBinding.apply {
            rvUser.visibility = GONE
            tvErrorMessage.apply {
                text = errorMessage
                setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                visibility = VISIBLE
            }
        }
    }

    private fun updatePageUI(companyNames : List<String>, users : List<User>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, companyNames)
        usersBinding.apply {
            rvUser.visibility = if (users.isEmpty()) GONE else VISIBLE
            tvErrorMessage.apply {
                setTextColor(Color.BLACK)
                text = getString(R.string.no_record_found)
                visibility = if (users.isEmpty()) VISIBLE else GONE
            }
            atCompany.apply {
                setAdapter(adapter)
                setOnItemClickListener { _, _, i, _ -> viewModel.onUserUIEvent(UsersUiEvent.ProcessCompanySelection(companyNames[i])) }
            }
        }
        userAdapter.submitList(users)
    }

    private fun updateUserList(users: List<User>) {
        val list = mutableListOf<User>().apply { addAll(users) }
        userAdapter.submitList(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _usersBinding = null
    }

    override fun onClick(view: View?) {
        view?.let {
            when(it) {
                usersBinding.ivSort -> viewModel.onUserUIEvent(UsersUiEvent.SortUser)
                usersBinding.ivRefresh -> viewModel.onUserUIEvent(UsersUiEvent.RequestUserData)
            }
        }
    }

}

sealed class UsersUiEvent {
    object RequestUserData : UsersUiEvent()
    data class ProcessCompanySelection(val companyName : String) : UsersUiEvent()
    object SortUser : UsersUiEvent()
}