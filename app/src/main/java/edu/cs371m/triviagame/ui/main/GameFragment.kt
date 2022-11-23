package edu.cs371m.triviagame.ui.main

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.resources.Compatibility.Api18Impl.setAutoCancel
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import edu.cs371m.triviagame.MainViewModel
import edu.cs371m.triviagame.Question
import edu.cs371m.triviagame.Questions
import edu.cs371m.triviagame.R
import edu.cs371m.triviagame.databinding.FragmentGameBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.*

class GameFragment : Fragment() {
    lateinit var currentQuestion: Question
    private val viewModel:MainViewModel by viewModels()
    private var questions: MutableList<Question> = mutableListOf()
    lateinit var answers: MutableList<String>
    private val numQuestions = 5
    private lateinit var tempAnsList: MutableList<String>
    private val args: GameFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var score = args.getScore
        var questionIndex = args.getQuestionIndexFromOtherFragment
        val passedData = args.getListFromOtherFragment

        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
            inflater, R.layout.fragment_game, container, false)

        if(questionIndex == 0){
            viewModel.netFetchData()
            viewModel.observeData().observe(viewLifecycleOwner){
                for(i in 0 until it.size){
                    tempAnsList = it.get(i).incorrectAnswers.toMutableList()
                    tempAnsList.add(0,it.get(i).correctAnswer)
                    questions.add(Question(text = it.get(i).question, answers = tempAnsList))
                }

                val asks = Questions()
                asks.add(questions)

                setQuestion(questionIndex)

                binding.game = this
                timer(binding,questionIndex,asks,score)

                binding.submitButton.setOnClickListener{ view: View ->
                    val secondsLeft = (binding.progressBar.progress * 10)/100
                    val checkedId = binding.questionRadioGroup.checkedRadioButtonId
                    // Do nothing if nothing is checked (id == -1)
                    if (checkedId != -1) {
                        var answerIndex = 0
                        when (checkedId) {
                            R.id.secondAnswerRadioButton -> answerIndex = 1
                            R.id.thirdAnswerRadioButton -> answerIndex = 2
                            R.id.fourthAnswerRadioButton -> answerIndex = 3
                        }

                        if (answers[answerIndex] == currentQuestion.answers[0]) {
                            if(score >= 200 && questionIndex > 1 && questionIndex < 4){
                                score = score+(20*secondsLeft*2)
                                binding.questionRadioGroup.clearCheck()
                                binding.invalidateAll()
                                view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToCorrectFragment(questionIndex,asks,score))
                            }else if(score >= 600 && questionIndex > 3){
                                score = score+(20*secondsLeft*3)
                                binding.questionRadioGroup.clearCheck()
                                binding.invalidateAll()
                                view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToCorrectFragment(questionIndex,asks,score))
                            }else{
                                score = score+(20*secondsLeft)
                                binding.questionRadioGroup.clearCheck()
                                binding.invalidateAll()
                                view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToCorrectFragment(questionIndex,asks,score))
                            }
                        } else {
                            binding.questionRadioGroup.clearCheck()
                            binding.invalidateAll()
                            view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment(questionIndex,asks,score))
                        }
                    }else{
                        Toast.makeText(this.context, "Must choose answer!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }else{
            val tempList = passedData.get(0)
            for(i in 0 until tempList.size){
                questions.add(Question(text = tempList.get(i).text, answers = tempList.get(i).answers))
            }

            val asks = Questions()
            asks.add(questions)

            setQuestion(questionIndex)

            binding.game = this
            timer(binding,questionIndex,asks,score)

            binding.submitButton.setOnClickListener{ view: View ->
                val secondsLeft = (binding.progressBar.progress * 10)/100
                val checkedId = binding.questionRadioGroup.checkedRadioButtonId
                // Do nothing if nothing is checked (id == -1)
                if (checkedId != -1) {
                    var answerIndex = 0
                    when (checkedId) {
                        R.id.secondAnswerRadioButton -> answerIndex = 1
                        R.id.thirdAnswerRadioButton -> answerIndex = 2
                        R.id.fourthAnswerRadioButton -> answerIndex = 3
                    }

                    if (answers[answerIndex] == currentQuestion.answers[0]) {
                        if(score >= 200 && questionIndex > 1 && questionIndex < 4){
                            score = score+(20*secondsLeft*2)
                            binding.questionRadioGroup.clearCheck()
                            binding.invalidateAll()
                            view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToCorrectFragment(questionIndex,asks,score))
                        }else if(score >= 600 && questionIndex > 3){
                            score = score+(20*secondsLeft*3)
                            binding.questionRadioGroup.clearCheck()
                            binding.invalidateAll()
                            view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToCorrectFragment(questionIndex,asks,score))
                        }else{
                            score = score+(20*secondsLeft)
                            binding.questionRadioGroup.clearCheck()
                            binding.invalidateAll()
                            view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToCorrectFragment(questionIndex,asks,score))
                        }
                    } else {
                        binding.questionRadioGroup.clearCheck()
                        binding.invalidateAll()
                        view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment(questionIndex,asks,score))
                    }
                }else{
                    Toast.makeText(this.context, "Must choose answer!", Toast.LENGTH_LONG).show()
                }
            }
        }
        return binding.root
    }

    private fun setQuestion(questionIndex: Int) {
        currentQuestion = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        Log.e("asd","answer key : "+answers[0])
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }

    private fun timer(binding:FragmentGameBinding,questionIndex:Int,asks:Questions,score:Int){
        val mProgressBar = binding.progressBar
        val progressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", 100, 0)
        progressAnimator.duration = 10000
        progressAnimator.interpolator = LinearInterpolator()
        progressAnimator.start()
        progressAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                view?.findNavController()?.navigate(GameFragmentDirections.actionGameFragmentToTimesUpFragment(questionIndex,asks,score))
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }
}
