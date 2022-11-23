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
import edu.cs371m.triviagame.databinding.FragmentLevelupBinding

class LevelUpFragment : Fragment() {
    private val args: LevelUpFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Techie"
        var questionIndex = args.getQuestionIndexFromCorrectFragment
        val passedData = args.getListFromCorrectFragment
        val score = args.getScore

        val binding: FragmentLevelupBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_levelup, container, false)

        binding.nextMatchButton.setOnClickListener { view: View ->
            view.findNavController().navigate(LevelUpFragmentDirections.actionLevelUpFragmentToGameFragment(questionIndex,passedData,score))
        }
        return binding.root
    }
}
