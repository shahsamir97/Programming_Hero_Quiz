package com.apps.programmingheroquiz.network

import com.apps.programmingheroquiz.model.QuizResponse
import retrofit2.http.GET

interface QuizApiService {

    @GET("quiz.json")
    suspend fun getQuestions(): QuizResponse
}
