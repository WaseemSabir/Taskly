package com.tajir.taskly.data.api.interfaces

import retrofit2.http.POST
import com.tajir.taskly.data.api.ApiConstants
import com.tajir.taskly.data.api.models.GeneralResponseAuth
import com.tajir.taskly.data.api.models.Login
import com.tajir.taskly.data.api.models.Register
import retrofit2.Response
import retrofit2.http.Body

interface AuthInterface {
    @POST(ApiConstants.REGISTER_ENDPOINT)
    suspend fun register(
        @Body data : Register
    ): Response<GeneralResponseAuth>

    @POST(ApiConstants.LOGIN_ENDPOINT)
    suspend fun login(
        @Body data : Login
    ): Response<GeneralResponseAuth>
}
