package edu.cs371m.triviagame.ui.main

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.cs371m.triviagame.R
import edu.cs371m.triviagame.database.UserData
import edu.cs371m.triviagame.databinding.FragmentGameWonBinding
import java.util.*

class GameWonFragment : Fragment() {
    private val args: GameWonFragmentArgs by navArgs()
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentGameWonBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_won, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Techie"

        val finalscoring = args.getScore

        startCountAnimation(binding,finalscoring,this.requireContext())

        binding.backToHome.visibility = View.INVISIBLE
        binding.addScore.visibility = View.INVISIBLE

        return binding.root
    }

    private fun startCountAnimation(binding:FragmentGameWonBinding,finalscore:Int,context:Context) {
        val animator = ValueAnimator.ofInt(0, finalscore)
        if(finalscore != 0){
            animator.duration = 2000
        }else{
            animator.duration = 0
        }
        animator.addUpdateListener { animation -> binding.finalscore.setText(animation.animatedValue.toString()) }
        animator.start()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                val userdata = UserData(
                    ownerName = user?.displayName.toString(),
                    ownerUid = user?.uid.toString(),
                    uuid = "987654321",
                    score = finalscore
                )
                val db: FirebaseFirestore = FirebaseFirestore.getInstance()

                binding.backToHome.visibility = View.VISIBLE
                binding.addScore.visibility = View.VISIBLE

                binding.backToHome.setOnClickListener { view: View ->
                    view.findNavController().navigate(GameWonFragmentDirections.actionGameWonFragmentToTitleFragment())
                }
                binding.addScore.setOnClickListener { view: View ->
                    db.collection("allData").document(user?.uid.toString()).set(userdata)
                    Toast.makeText(context, "Score added to Leaderboard!", Toast.LENGTH_LONG).show()
                }
                setHasOptionsMenu(true)
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    // Creating our Share Intent
    private fun getShareIntent() : Intent {
        val finalscore = args.getScore
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_success_text, finalscore))
        return shareIntent
    }

    // Starting an Activity with our new Intent
    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    // Showing the Share Menu Item Dynamically
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.winner_menu, menu)
        // check if the activity resolves
        if (null == getShareIntent().resolveActivity(requireActivity().packageManager)) {
            // hide the menu item if it doesn't resolve
            menu.findItem(R.id.share)?.isVisible = false
        }
    }

    // Sharing from the Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}
