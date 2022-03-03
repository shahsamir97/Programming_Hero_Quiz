package com.apps.programmingheroquiz.ui.quiz_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apps.programmingheroquiz.model.Question
import com.apps.programmingheroquiz.network.QuizApiService
import com.apps.programmingheroquiz.utils.NetworkCallStatus
import okio.IOException
import retrofit2.HttpException

class QuizRepository(private val apiService: QuizApiService) {

    private val _networkCallStatus = MutableLiveData<NetworkCallStatus>()
    val networkCallStatus: LiveData<NetworkCallStatus>
    get() = _networkCallStatus

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>>
    get() = _questions

    suspend fun getQuestionsFromRemoteSource(){
        _networkCallStatus.value = NetworkCallStatus.LOADING
        try {
            val data = apiService.getQuestions()
            _questions.value = data.questions
            _networkCallStatus.value = NetworkCallStatus.SUCCESS
        } catch (e: IOException) {
            e.printStackTrace()
            _networkCallStatus.value = NetworkCallStatus.ERROR
        }catch (e: HttpException){
            e.printStackTrace()
            _networkCallStatus.value = NetworkCallStatus.ERROR
        }
    }
}