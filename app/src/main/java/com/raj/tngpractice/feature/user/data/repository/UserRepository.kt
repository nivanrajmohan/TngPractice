package com.raj.tngpractice.feature.user.data.repository

import com.raj.tngpractice.feature.general.model.Resource

interface UserRepository {

    suspend fun fetchAllUsers() : Resource<Any?>

}