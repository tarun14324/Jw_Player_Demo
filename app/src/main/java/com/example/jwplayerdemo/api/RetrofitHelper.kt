package com.example.jwplayerdemo.api


import com.example.jwplayerdemo.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

   private const val baseUrl = "https://stage-nlearn.gcf.education/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
            //Adding App Version as Header to every api
             .addHeader("versionCode", "50")
            .addHeader("versionName", "3.3.9.debug")
            .addHeader("platform", "mobile")
            .addHeader("isSureMDM", "false")
            .addHeader("source", "nlearn")
            .addHeader("Authorization", "Bearer "+AppConstants.BearerToken)
            .addHeader("admissionNumber", "mgmt11learn156")
            chain.proceed(request.build())
        }
        .build()


    fun getInstance(): ApiInterface {
        val retro= Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return  retro.create(ApiInterface::class.java )
    }
}
