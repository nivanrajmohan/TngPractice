package com.raj.tngpractice.feature.user.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raj.tngpractice.R
import com.raj.tngpractice.feature.general.model.Resource
import com.raj.tngpractice.feature.user.data.remote.UserAPI
import com.raj.tngpractice.feature.user.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
   private val userAPI: UserAPI,
   private val ioDispatcher: CoroutineDispatcher,
   @ApplicationContext private val appContext : Context
) : UserRepository {

    override suspend fun fetchAllUsers(): Resource<Any?> = withContext(ioDispatcher) {
        return@withContext try {
            val request = userAPI.fetchAllUsers()
            if (request.isSuccessful) {
                if (request.body().isNullOrEmpty()) {
                    Resource.Error(appContext.getString(R.string.data_not_found), 0)
                } else {
                    val userList = Gson().fromJson<List<User>>(request.body(),
                        object : TypeToken<Collection<User>>(){}.type)
                    Resource.Success(userList)
                }
            } else {
                Resource.Error(request.errorBody().toString(), 0)
            }
        } catch (e : Exception) {
            Resource.Error(e.message.toString(), 0)
        }
    }

}