package com.campuscloset.gsu.network

import com.campuscloset.gsu.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseClient {

    private val authInterceptor = Interceptor { chain ->
        val req = chain.request().newBuilder()
            .addHeader("apikey", BuildConfig.SUPABASE_ANON_KEY)
            .addHeader("Authorization", "Bearer ${BuildConfig.SUPABASE_ANON_KEY}")
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(req)
    }

    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logger)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SUPABASE_URL + "/rest/v1/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: SupabaseApi = retrofit.create(SupabaseApi::class.java)


}
