package com.tajir.taskly.data.api.repository

import com.tajir.taskly.data.api.models.GeneralResponseAuth
import com.tajir.taskly.data.api.models.Register
import retrofit2.Response
import com.tajir.taskly.data.api.RetrofitInstance
import com.tajir.taskly.data.api.models.Login

class AuthRepository {
    suspend fun register(data : Register): Response<GeneralResponseAuth> {
        return RetrofitInstance.authApi.register(data = data)
    }

    suspend fun login(data: Login): Response<GeneralResponseAuth> {
        return RetrofitInstance.authApi.login(data = data)
    }
}
