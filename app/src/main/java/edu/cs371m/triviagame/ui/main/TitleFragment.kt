package edu.cs371m.triviagame.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.cs371m.triviagame.*
import edu.cs371m.triviagame.database.UniqueName
import edu.cs371m.triviagame.database.UserData
import edu.cs371m.triviagame.databinding.FragmentTitleBinding
import java.util.*

class TitleFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()){}
    private var _binding: FragmentTitleBinding? = null
    private val binding get() = _binding!!
    private val passToGame = Questions()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        (activity as AppCompatActivity).supportActionBar?.title = "Techie"
        val currentUser = FirebaseAuth.getInstance().currentUser

        if(currentUser != null){
            if(currentUser.displayName == currentUser.email || currentUser.displayName == null){
                val defaultDisplayName = currentUser.email
                AuthInit.setDisplayName(defaultDisplayName.toString(),viewModel)
            }
        }

        val displayname = currentUser?.displayName
        binding.welcomeMessage.text = "Hi "+displayname+",\nlet's play some game!"

        binding.playButton.setOnClickListener { view : View ->
            view.findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment(0,passToGame,0))
        }
        setHasOptionsMenu(true)
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == 2131230734){
            viewModel.signOut()
            AuthInit(signInLauncher)
        }
        return super.onOptionsItemSelected(item)
    }

}
