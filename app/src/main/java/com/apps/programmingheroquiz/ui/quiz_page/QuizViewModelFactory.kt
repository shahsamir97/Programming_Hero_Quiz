package com.apps.programmingheroquiz.ui.quiz_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class QuizViewModelFactory(private val repository: QuizRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java))
            return QuizViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}