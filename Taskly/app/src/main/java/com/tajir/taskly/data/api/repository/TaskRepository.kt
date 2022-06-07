package com.tajir.taskly.data.api.repository

import com.tajir.taskly.data.api.RetrofitInstance
import com.tajir.taskly.data.api.models.GeneralResponseTasks
import com.tajir.taskly.data.api.models.Task
import com.tajir.taskly.data.api.models.TaskCreate
import com.tajir.taskly.data.api.models.TaskUpdate
import retrofit2.Response

class TaskRepository {
    suspend fun retrieve(token : String, tid : String): Response<GeneralResponseTasks> {
        val bearerAuth = String.format("Bearer %s", token)
        return RetrofitInstance.taskApi.retrieve(taskID = tid, auth = bearerAuth)
    }

    suspend fun retrieve_all(token : String): Response<GeneralResponseTasks> {
        val bearerAuth = String.format("Bearer %s", token)
        return RetrofitInstance.taskApi.retrieve_all(auth = bearerAuth)
    }

    suspend fun new(token : String, data : TaskCreate): Response<GeneralResponseTasks> {
        val bearerAuth = String.format("Bearer %s", token)
        return RetrofitInstance.taskApi.new(auth = bearerAuth, data = data)
    }

    suspend fun edit(token : String, tid : String, data : TaskUpdate): Response<GeneralResponseTasks> {
        val bearerAuth = String.format("Bearer %s", token)
        return RetrofitInstance.taskApi.edit(taskID = tid, auth = bearerAuth, data = data)
    }

    suspend fun remove(token: String, tid: String): Response<GeneralResponseTasks> {
        val bearerAuth = String.format("Bearer %s", token)
        return RetrofitInstance.taskApi.remove(taskID = tid, auth = bearerAuth)
    }
}
