package com.tajir.taskly.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.tajir.taskly.data.api.interfaces.AuthInterface
import com.tajir.taskly.data.api.interfaces.UserInterface

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthInterface by lazy {
        retrofit.create(AuthInterface::class.java)
    }

    val userApi: UserInterface by lazy {
        retrofit.create(UserInterface::class.java)
    }

}
