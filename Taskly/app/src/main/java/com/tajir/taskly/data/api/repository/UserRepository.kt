package com.tajir.taskly.data.api.repository

import com.tajir.taskly.data.api.RetrofitInstance
import com.tajir.taskly.data.api.models.GeneralResponseUser
import com.tajir.taskly.data.api.models.User
import com.tajir.taskly.data.api.models.UserWithPassword
import retrofit2.Response

class UserRepository {
    suspend fun retrieve(token : String): Response<GeneralResponseUser> {
        val bearerAuth = String.format("Bearer %s", token)
        return RetrofitInstance.userApi.retrieve(auth = bearerAuth)
    }

    suspend fun remove(token : String): Response<GeneralResponseUser> {
        val bearerAuth = String.format("Bearer %s", token)
        return RetrofitInstance.userApi.remove(auth = bearerAuth)
    }

    suspend fun edit(data : User, token: String): Response<GeneralResponseUser> {
        val bearerAuth = String.format("Bearer %s", token)
        return RetrofitInstance.userApi.edit(data = data, auth = bearerAuth)
    }
}
