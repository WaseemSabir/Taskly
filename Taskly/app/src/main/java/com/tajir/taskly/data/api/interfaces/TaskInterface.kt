package com.tajir.taskly.data.api.interfaces

import com.tajir.taskly.data.api.ApiConstants
import com.tajir.taskly.data.api.models.*
import retrofit2.Response
import retrofit2.http.*

interface TaskInterface {
    @GET(ApiConstants.TASK_ENDPOINT+"/{task_id}")
    suspend fun retrieve(
        @Path(value = "task_id", encoded = true) taskID : String,
        @Header("Authorization") auth : String
    ): Response<GeneralResponseTasks>

    @GET(ApiConstants.TASK_ALL_ENDPOINT)
    suspend fun retrieve_all(
        @Header("Authorization") auth : String
    ): Response<GeneralResponseTasks>

    @POST(ApiConstants.TASK_CREATE_ENDPOINT)
    suspend fun new(
        @Header("Authorization") auth : String,
        @Body data : TaskCreate
    ): Response<GeneralResponseTasks>

    @PUT(ApiConstants.TASK_ENDPOINT+"/{task_id}")
    suspend fun edit(
        @Path(value = "task_id", encoded = true) taskID : String,
        @Header("Authorization") auth : String,
        @Body data : TaskUpdate
    ): Response<GeneralResponseTasks>

    @DELETE(ApiConstants.TASK_ENDPOINT+"/{task_id}")
    suspend fun remove(
        @Path(value = "task_id", encoded = true) taskID : String,
        @Header("Authorization") auth : String
    ): Response<GeneralResponseTasks>
}
