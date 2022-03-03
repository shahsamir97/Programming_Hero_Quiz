package com.apps.programmingheroquiz.ui.main_menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.apps.programmingheroquiz.R
import com.apps.programmingheroquiz.databinding.FragmentMainMenuBinding
import com.apps.programmingheroquiz.network.ServiceGenerator
import com.apps.programmingheroquiz.ui.quiz_page.QuizRepository

class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding


    private val viewModel by viewModels<MainMenuViewModel> {
        MainMenuVMFactory(QuizRepository(ServiceGenerator.quizApiService))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragmen
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.quizPage)
        }
    }
}