package com.apps.programmingheroquiz.ui.main_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.programmingheroquiz.ui.quiz_page.QuizRepository
import java.lang.IllegalArgumentException

class MainMenuVMFactory(private val repository: QuizRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainMenuViewModel::class.java))
            return MainMenuViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}