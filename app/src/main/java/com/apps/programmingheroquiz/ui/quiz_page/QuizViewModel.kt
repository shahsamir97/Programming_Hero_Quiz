package com.apps.programmingheroquiz.ui.quiz_page

import android.webkit.URLUtil
import androidx.lifecycle.*
import com.apps.programmingheroquiz.model.Question
import com.apps.programmingheroquiz.utils.NetworkCallStatus
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    //TODO:: if the variables are not used publicly remove the backing properties

    private lateinit var correctAnswer: String
    private val questions: LiveData<List<Question>> = repository.questions

    private val _currentQuestionInfo = MutableLiveData<Question>()
    private val currentQuestionInfo: LiveData<Question>
        get() = _currentQuestionInfo

    private val _currentAnswers = MutableLiveData<Map<String, String>>()
    val currentAnswers: LiveData<Map<String, String>>
        get() = _currentAnswers

    private val _currentlyDisplayingQuestion = MutableLiveData<String>()
    val currentlyDisplayingQuestion: LiveData<String>
        get() = _currentlyDisplayingQuestion

    private val _currentQuesImgUrl = MutableLiveData<String?>()
    val currentQuesImgUrl: LiveData<String?>
        get() = _currentQuesImgUrl

    private val _possibleScoreForCurrentQues = MutableLiveData<Int>()
    val possibleScoreForCurrentQues: LiveData<Int>
        get() = _possibleScoreForCurrentQues

    private val _quizProgress = MutableLiveData<String>()
    val quizProgress: LiveData<String>
    get() = _quizProgress

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private val completedQuestions = MutableLiveData(0)

    private val networkCallStatus: LiveData<NetworkCallStatus> = repository.networkCallStatus

    val currentScore = MutableLiveData(0)

    val isDataLoading: LiveData<Boolean>
        get() = Transformations.map(networkCallStatus) {
            it == NetworkCallStatus.LOADING
        }

    var questionCount = 0

    init {
        viewModelScope.launch {
            repository.getQuestionsFromRemoteSource()

            if (networkCallStatus.value == NetworkCallStatus.SUCCESS) {
                startQuiz()
                _quizProgress.value = "Question: " + completedQuestions.value.toString() + "/" + questions.value!!.size
            } else {
                _toastMessage.value =
                    "Unable to start the quiz! Please check your internet connection"
            }
        }
    }

     fun startQuiz() {
        if (questionCount != 0) Thread.sleep(2000)

        _currentQuestionInfo.value = findQuestion(questionCount)
        _currentAnswers.value = _currentQuestionInfo.value!!.answers
        _currentlyDisplayingQuestion.value = currentQuestionInfo.value!!.question
        _currentQuesImgUrl.value = currentQuestionInfo.value!!.questionImageUrl
        _possibleScoreForCurrentQues.value = currentQuestionInfo.value!!.score
        correctAnswer = getCorrectAnswer(_currentQuestionInfo.value!!.correctAnswer)
    }

    private fun findQuestion(position: Int): Question {
        return questions.value!![position]
    }

    private fun getCorrectAnswer(answerKey: String): String {
        return _currentAnswers.value?.get(answerKey)!!
    }

    fun verifyAnswer(answer: String, verifyFeedback: (isCorrect: Boolean) -> Unit) {
        if (answer == correctAnswer) {
            verifyFeedback(true)
            currentScore.value = currentScore.value?.plus(_possibleScoreForCurrentQues.value!!)
        } else {
            verifyFeedback(false)
        }

        completedQuestions.value = completedQuestions.value?.plus(1)
        _quizProgress.value = "Question: " + completedQuestions.value.toString() + "/" + questions.value!!.size

        if (questionCount < questions.value!!.size - 1) {
            questionCount++
        }
    }
}