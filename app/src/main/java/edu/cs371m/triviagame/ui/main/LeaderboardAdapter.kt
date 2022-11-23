package edu.cs371m.triviagame.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.triviagame.MainViewModel
import edu.cs371m.triviagame.database.UserData
import edu.cs371m.triviagame.databinding.LeaderboardBinding

class LeaderboardAdapter(val name:MutableList<String>, val score:MutableList<Int>): RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LeaderboardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(holder: ViewHolder, position: Int) {
            val playername = name[position]
            val playerscore = score[position]
            val rank = position+1
            holder.binding.rank.text = rank.toString()
            holder.binding.playerName.text = playername
            holder.binding.playerScore.text = playerscore.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LeaderboardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holder,position)
    }

    override fun getItemCount(): Int {
        return name.size
    }

}
