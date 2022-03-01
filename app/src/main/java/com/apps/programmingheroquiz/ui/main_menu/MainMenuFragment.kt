package com.apps.programmingheroquiz.ui.main_menu

import android.app.Service
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.apps.programmingheroquiz.R
import com.apps.programmingheroquiz.network.ServiceGenerator

class MainMenuFragment : Fragment() {

    private val viewModel by viewModels<MainMenuViewModel> {
        MainMenuVMFactory(QuizRepository(ServiceGenerator.quizApiService))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel
    }
}