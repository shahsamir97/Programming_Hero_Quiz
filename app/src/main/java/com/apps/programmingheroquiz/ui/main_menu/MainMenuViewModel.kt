package com.apps.programmingheroquiz.ui.main_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainMenuViewModel(private val repository: QuizRepository): ViewModel() {

    init {
        viewModelScope.launch { repository.getQuestionsFromRemoteSource() }
    }
}