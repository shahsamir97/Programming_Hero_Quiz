package com.apps.programmingheroquiz.ui.quiz_page

import androidx.lifecycle.*
import com.apps.programmingheroquiz.model.Question
import com.apps.programmingheroquiz.utils.NetworkCallStatus
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

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
    val possibleScoreForCurrentQues:LiveData<String>
    get() = Transformations.map(_possibleScoreForCurrentQues){scoreValue ->
        "$scoreValue Point"
    }

    private val _quizProgress = MutableLiveData<String>()
    val quizProgress: LiveData<String>
        get() = _quizProgress

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    val currentScoreValue = MutableLiveData(0)
    val currentScore: LiveData<String>
    get() = Transformations.map(currentScoreValue){ currentScore->
        "Score: $currentScore"
    }

    private val networkCallStatus: LiveData<NetworkCallStatus> = repository.networkCallStatus
    val isDataLoading: LiveData<Boolean>
        get() = Transformations.map(networkCallStatus) {
            it == NetworkCallStatus.LOADING
        }

    private val _isQuizFinished = MutableLiveData<Boolean>()
    val isQuizFinished: LiveData<Boolean>
    get() = _isQuizFinished

    private var questionCount = 0
    private var questionNumber = 1

    init {
        viewModelScope.launch {
            repository.getQuestionsFromRemoteSource()

            if (networkCallStatus.value == NetworkCallStatus.SUCCESS) {
                startQuiz()
            } else {
                _toastMessage.value =
                    "Unable to start the quiz! Please check your internet connection"
            }
        }
    }

    fun startQuiz() {
        if (questionCount < questions.value!!.size) {
            _currentQuestionInfo.value = findQuestion(questionCount)
            _currentAnswers.value = _currentQuestionInfo.value!!.answers
            _currentlyDisplayingQuestion.value = currentQuestionInfo.value!!.question
            _currentQuesImgUrl.value = currentQuestionInfo.value!!.questionImageUrl
            _possibleScoreForCurrentQues.value = currentQuestionInfo.value!!.score
            correctAnswer = getCorrectAnswer(_currentQuestionInfo.value!!.correctAnswer)
            _quizProgress.value =
                "Question: " + questionNumber + "/" + questions.value!!.size
        }
    }

    private fun findQuestion(position: Int): Question {
        return questions.value!![position]
    }

    private fun getCorrectAnswer(answerKey: String): String {
        return _currentAnswers.value?.get(answerKey)!!
    }

    fun verifyAnswer(answer: String, verificationFeedback: (isCorrect: Boolean, correctAnswer: String) -> Unit) {
        if (answer == correctAnswer) {
            currentScoreValue.value = currentScoreValue.value?.plus(_possibleScoreForCurrentQues.value!!)
            updateQuizProgressValues()
            verificationFeedback(true, correctAnswer)
        } else {
            updateQuizProgressValues()
            verificationFeedback(false, correctAnswer)
        }
    }

    private fun updateQuizProgressValues() {
        if (questionCount < questions.value!!.size) {
            if (questionNumber < questions.value!!.size) questionNumber++
            questionCount++
            if (questionCount == questions.value!!.size) quizFinished()
        }

    }

    private fun quizFinished(){
        _isQuizFinished.value = true
    }


}