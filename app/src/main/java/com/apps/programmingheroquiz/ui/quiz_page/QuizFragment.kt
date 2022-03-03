package com.apps.programmingheroquiz.ui.quiz_page

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.apps.programmingheroquiz.R
import com.apps.programmingheroquiz.databinding.FragmentQuizPageBinding
import com.apps.programmingheroquiz.network.ServiceGenerator
import com.apps.programmingheroquiz.utils.HIGH_SCORE_KEY
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class QuizFragment : Fragment() {

    private val viewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(QuizRepository(ServiceGenerator.quizApiService))
    }
    private lateinit var binding: FragmentQuizPageBinding
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz_page, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.quizData = viewModel

        viewModel.toastMessage.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.currentAnswers.observe(viewLifecycleOwner) {
            val answersLayout = binding.answerLayout
            answersLayout.removeAllViews()

            val shuffledAnswers = getShuffledAnswers(it)
            for (options in shuffledAnswers) {
                createAnswerButton(options, answersLayout)
            }

            startTimer()
        }

        viewModel.isQuizFinished.observe(viewLifecycleOwner){ isQuizFinished ->
            if (isQuizFinished){
                Toast.makeText(requireActivity().baseContext, "Quiz Finished", Toast.LENGTH_SHORT).show()
                Toast.makeText(
                    requireContext(),
                    "Your Score: " + viewModel.currentScoreValue.value,
                    Toast.LENGTH_SHORT
                ).show()

                val highScore = sharedPref.getInt(HIGH_SCORE_KEY, 0)
                if (highScore < viewModel.currentScoreValue.value!!) {
                    sharedPref.edit()
                        .putInt(HIGH_SCORE_KEY, viewModel.currentScoreValue.value!!.toInt()).apply()
                }
                waitTwoSecondThenExecute {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun createAnswerButton(
        option: String,
        answersLayout: LinearLayout
    ) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(10, 10, 10, 10)

        val button = MaterialButton(requireContext())
        button.setPadding(10, 0, 10, 0)
        button.text = option
        button.setTextColor(Color.BLACK)
        button.textAlignment = View.TEXT_ALIGNMENT_CENTER
        button.setBackgroundColor(Color.WHITE)
        button.setOnClickListener(answerOnClickListener)
        answersLayout.addView(button, layoutParams)
    }

    private val answerOnClickListener = View.OnClickListener { (it as MaterialButton)
        stopTimer()
        disableAnswerButtons()
        getAnswerVerified(it)

        waitTwoSecondThenExecute {
            viewModel.startQuiz()
        }
    }

    private fun disableAnswerButtons() {
        binding.answerLayout.children.forEach {
            (it as MaterialButton)
            it.isEnabled = false
        }
    }

    private fun getAnswerVerified(answer: MaterialButton) {
        viewModel.verifyAnswer(answer.text.toString()) { isCorrect, correctAnswer ->
            if (isCorrect) {
                answer.strokeWidth = 15
                answer.setStrokeColorResource(android.R.color.holo_green_dark)
            } else {
                highLightCorrectAnswer(correctAnswer)

                answer.strokeWidth = 15
                answer.setStrokeColorResource(android.R.color.holo_red_dark)
            }
        }
    }

    private fun highLightCorrectAnswer(correctAnswer: String) {
        binding.answerLayout.children.forEach {
            (it as MaterialButton)
            if (it.text == correctAnswer) {
                it.strokeWidth = 15
                it.setStrokeColorResource(android.R.color.holo_green_dark)
            }
        }
    }

    private fun getShuffledAnswers(questions: Map<String,String>): List<String> {
        val values: List<String> = ArrayList(questions.values)
        Collections.shuffle(values)
        return values
    }

    private fun waitTwoSecondThenExecute(task:()->Unit){
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                Thread.sleep(2000)
            }
            task()
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(10000, 100) {
            var increment = 5
            override fun onTick(millisUntilFinished: Long) {
                increment++
                binding.progressBar.progress = increment
            }

            override fun onFinish() {
                disableAnswerButtons()
                highLightCorrectAnswer(viewModel.getCorrectAnswer())
                waitTwoSecondThenExecute {
                    viewModel.nextQuestion()
                }
            }
        }
        countDownTimer.start()
    }

    fun stopTimer(){
        countDownTimer.cancel()
    }

}
