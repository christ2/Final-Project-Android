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
import edu.cs371m.triviagame.databinding.FragmentTimesupBinding

class TimesUpFragment : Fragment() {
    private val args: TimesUpFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Techie"
        var questionIndex = args.getQuestionIndexFromGameFragment
        val passedData = args.getListFromGameFragment
        val score = args.getScore

        val binding: FragmentTimesupBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_timesup, container, false)

        questionIndex++

        binding.tryAgainButton.setOnClickListener { view: View ->
            if(questionIndex < 5){
                view.findNavController().navigate(TimesUpFragmentDirections.actionTimesUpFragmentToGameFragment(questionIndex,passedData,score))
            }else{
                view.findNavController().navigate(TimesUpFragmentDirections.actionTimesUpFragmentToGameWonFragment(score))
            }
        }
        return binding.root
    }
}
