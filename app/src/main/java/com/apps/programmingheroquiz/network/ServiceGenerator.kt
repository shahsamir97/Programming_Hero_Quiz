package com.apps.programmingheroquiz.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


 class ServiceGenerator {


    companion object{
        const val BASE_URL = "https://herosapp.nyc3.digitaloceanspaces.com/"
        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val quizApiService: QuizApiService by lazy {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()


            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()

            retrofit.create(QuizApiService::class.java)
        }
    }
}