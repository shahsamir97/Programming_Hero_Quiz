package com.apps.programmingheroquiz.ui.quiz_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository): ViewModel() {

    init {
        viewModelScope.launch { repository.getQuestionsFromRemoteSource() }
    }
}