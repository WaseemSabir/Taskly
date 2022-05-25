package com.tajir.taskly.data.api.interfaces

import com.tajir.taskly.data.api.ApiConstants
import com.tajir.taskly.data.api.models.*
import retrofit2.Response
import retrofit2.http.*

interface UserInterface {
    @GET(ApiConstants.USER_ENDPOINT)
    suspend fun retrieve(@Header("Authorization") auth : String): Response<GeneralResponseUser>

    @PUT(ApiConstants.USER_ENDPOINT)
    suspend fun edit(
        @Header("Authorization") auth : String,
        @Body data : UserWithPassword
    ): Response<GeneralResponseUser>

    @DELETE(ApiConstants.USER_ENDPOINT)
    suspend fun remove(@Header("Authorization") auth : String): Response<GeneralResponseUser>

}
