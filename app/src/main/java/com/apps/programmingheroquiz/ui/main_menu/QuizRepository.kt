package com.apps.programmingheroquiz.ui.main_menu

import android.util.Log
import com.apps.programmingheroquiz.network.QuizApiService
import okio.IOException
import retrofit2.HttpException

class QuizRepository(private val apiService: QuizApiService) {

    suspend fun getQuestionsFromRemoteSource(){
        try {
            val data = apiService.getQuestions()
            Log.i("questions ::: ", data.questions.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }catch (e: HttpException){
            e.printStackTrace()
        }
    }
}