package com.usfzy.geoquiz

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.usfzy.geoquiz.databinding.ActivityMainBinding
import com.usfzy.geoquiz.viewmodel.QuizViewModel

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener(this)
        binding.falseButton.setOnClickListener(this)
        binding.nextButton.setOnClickListener(this)
        binding.cheatButton.setOnClickListener(this)

        updateQuestion()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.nextButton.id -> {
                quizViewModel.moveToNext()
                updateQuestion()
            }

            binding.trueButton.id -> {
                checkAnswer(true)
            }

            binding.falseButton.id -> {
                checkAnswer(false)
            }

            binding.cheatButton.id -> {
                val answerIsTrue = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
                cheatLauncher.launch(intent)
            }
        }
    }

    private fun updateQuestion() {
        val questionResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgement_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}