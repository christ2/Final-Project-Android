package edu.cs371m.triviagame.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import edu.cs371m.triviagame.R
import edu.cs371m.triviagame.databinding.FragmentGameOverBinding

class GameOverFragment : Fragment() {
    private val args: GameOverFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Techie"
        var questionIndex = args.getQuestionIndexFromGameFragment
        val passedData = args.getListFromGameFragment
        val score = args.getScore

        val binding: FragmentGameOverBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_over, container, false)

        questionIndex++

        binding.tryAgainButton.setOnClickListener { view: View ->
            if(questionIndex < 5){
                view.findNavController().navigate(GameOverFragmentDirections.actionGameOverFragmentToGameFragment(questionIndex,passedData,score))
            }else{
                view.findNavController().navigate(GameOverFragmentDirections.actionGameOverFragmentToGameWonFragment(score))
            }
        }
        return binding.root
    }
}
