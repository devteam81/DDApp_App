package com.dd.app.dd.network

import com.dd.app.BuildConfig
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.network.ApiConstants.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val retrofitClient: Retrofit.Builder by lazy {

        val levelType: HttpLoggingInterceptor.Level
        if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
            levelType = HttpLoggingInterceptor.Level.BODY else levelType =
            HttpLoggingInterceptor.Level.NONE

        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)

        val okhttpClient = OkHttpClient.Builder()
        okhttpClient.addInterceptor(logging)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor(Interceptor { chain ->
            val request: Request =
                chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer "+ LoginUser.authToken)
                    .addHeader("app_id", "6124a1dec83ed94d9cca372e")
                    .addHeader("App-Id", "6124a1dec83ed94d9cca372e")
                    .addHeader("App-Live", "false")
                    .build()
            chain.proceed(request)

        })

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            //.client(okhttpClient.build())
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }

}