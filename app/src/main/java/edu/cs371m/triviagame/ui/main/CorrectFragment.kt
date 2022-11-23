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
import edu.cs371m.triviagame.databinding.FragmentCorrectBinding
import edu.cs371m.triviagame.databinding.FragmentGameOverBinding

class CorrectFragment : Fragment() {
    private val args: CorrectFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Techie"

        var questionIndex = args.getQuestionIndexFromGameFragment
        val passedData = args.getListFromGameFragment
        val score = args.getScore

        val binding: FragmentCorrectBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_correct, container, false)

        questionIndex++

        binding.nextMatchButton.setOnClickListener { view: View ->
            if(questionIndex < 5){
                if(score >= 200 && questionIndex == 2){
                    view.findNavController().navigate(CorrectFragmentDirections.actionCorrectFragmentToLevelUpFragment(questionIndex,passedData,score))
                }else if(score >= 600 && questionIndex == 4){
                    view.findNavController().navigate(CorrectFragmentDirections.actionCorrectFragmentToLevelUpTwoFragment(questionIndex,passedData,score))
                }else{
                    view.findNavController().navigate(CorrectFragmentDirections.actionCorrectFragmentToGameFragment(questionIndex,passedData,score))
                }
            }else{
                view.findNavController().navigate(CorrectFragmentDirections.actionCorrectFragmentToGameWonFragment(score))
            }
        }

        return binding.root
    }
}
