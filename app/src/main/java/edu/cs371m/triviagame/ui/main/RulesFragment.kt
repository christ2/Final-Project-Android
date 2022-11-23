package edu.cs371m.triviagame.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.cs371m.triviagame.MainViewModel
import edu.cs371m.triviagame.R
import edu.cs371m.triviagame.databinding.FragmentAboutBinding
import edu.cs371m.triviagame.databinding.FragmentGameWonBinding
import edu.cs371m.triviagame.databinding.FragmentRulesBinding

class RulesFragment : Fragment() {
    private var _binding: FragmentRulesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentRulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Techie"

        val user = FirebaseAuth.getInstance().currentUser
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        var listOfData: MutableList<List<Any>> = mutableListOf()
        var tempListUserName: MutableList<String> = mutableListOf()
        var tempListScore: MutableList<Int> = mutableListOf()

        db.collection("allData").orderBy("score", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    listOfData.add(document.data.values.toList())
                }

                for(i in 0 until listOfData.size){
                    tempListUserName.add(listOfData.get(i).get(2).toString())
                    tempListScore.add(Integer.parseInt(listOfData.get(i).get(1).toString()))
                }

                val adapter = LeaderboardAdapter(tempListUserName,tempListScore)
                val rv = binding.recyclerView
                val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
                rv.addItemDecoration(itemDecor)
                rv.adapter = adapter
                rv.layoutManager = LinearLayoutManager(rv.context)
            }

        binding.clearRecord.setOnClickListener{
            db.collection("allData").document(user?.uid.toString())
                .delete()
                .addOnSuccessListener {
                    Log.d("asd", "data successfully deleted!")
                }

            val value = tempListUserName.indexOf(user?.displayName)

            if(value != -1){
                Toast.makeText(context, "Your record has been removed!", Toast.LENGTH_LONG).show()
                tempListScore.removeAt(value)
                tempListUserName.removeAt(value)

                val adapter = LeaderboardAdapter(tempListUserName,tempListScore)
                val rv = binding.recyclerView
                val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
                rv.addItemDecoration(itemDecor)
                rv.adapter = adapter
                rv.layoutManager = LinearLayoutManager(rv.context)
            }else{
                Toast.makeText(context, "Your record already removed!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
