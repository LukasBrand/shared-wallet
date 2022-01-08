package com.lukasbrand.sharedwallet.services.message.api

import com.lukasbrand.sharedwallet.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://fcm.googleapis.com/fcm/send/"

    private val logging = HttpLoggingInterceptor()

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .header("Authorization", "key=${BuildConfig.FIREBASE_MESSAGING_API_KEY}")
                .build()
            chain.proceed(newRequest)
        }.addInterceptor(logging)
        .build()

    private fun getRetrofit(): Retrofit {
        logging.level = HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val sendApiService: SendApiService = getRetrofit().create(SendApiService::class.java)
}
