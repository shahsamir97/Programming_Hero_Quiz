package com.apps.programmingheroquiz.ui.main_menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apps.programmingheroquiz.R
import com.apps.programmingheroquiz.databinding.FragmentMainMenuBinding
import com.apps.programmingheroquiz.utils.HIGH_SCORE_KEY

class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val highScore = sharedPref.getInt(HIGH_SCORE_KEY, 0)

        binding.highScore.text = getString(R.string.high_score, highScore.toString())

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.quizPage)
        }
    }
}