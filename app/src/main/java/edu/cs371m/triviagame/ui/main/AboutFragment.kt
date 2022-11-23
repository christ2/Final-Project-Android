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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.cs371m.triviagame.AuthInit
import edu.cs371m.triviagame.MainViewModel
import edu.cs371m.triviagame.R
import edu.cs371m.triviagame.database.UniqueName
import edu.cs371m.triviagame.databinding.FragmentAboutBinding
import edu.cs371m.triviagame.databinding.FragmentTitleBinding

class AboutFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentAboutBinding? = null
    private var listOfUser: MutableList<String> = mutableListOf()
    private val binding get() = _binding!!
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Techie"
        val user = FirebaseAuth.getInstance().currentUser

        binding.displayNameET.setText(user?.displayName)

        binding.submitUsername.setOnClickListener {
            if(binding.displayNameET.text.isNullOrBlank()){
                Toast.makeText(this.context, "Username cannot be empty!", Toast.LENGTH_LONG).show()
            }else{
                db.collection("allUser")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            listOfUser.add(document.id)
                        }

                        if(listOfUser.contains(binding.displayNameET.text.toString())){
                            Toast.makeText(this.context, "Username already exist!", Toast.LENGTH_LONG).show()
                        }else{
                            db.collection("allUser").document(user?.displayName.toString()).delete()
                            AuthInit.setDisplayName(binding.displayNameET.getText().toString(),viewModel)
                            db.collection("allData").document(user?.uid.toString()).update("ownerName",binding.displayNameET.getText().toString())
                            Toast.makeText(this.context, "Username has been successfully changed!", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}