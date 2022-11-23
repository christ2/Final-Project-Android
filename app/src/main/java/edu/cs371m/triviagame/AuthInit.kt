package edu.cs371m.triviagame

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import edu.cs371m.triviagame.database.UniqueName

class AuthInit(signInLauncher: ActivityResultLauncher<Intent>) {
    companion object {
        private const val TAG = "AuthInit"
        fun setDisplayName(displayName : String, viewModel: MainViewModel) {
            if(FirebaseAuth.getInstance().currentUser != null){
                val user = FirebaseAuth.getInstance().currentUser
                val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(displayName).build()
                val unique = UniqueName(ownerName = "")
                val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        db.collection("allUser").document(user?.displayName.toString()).set(unique)
                        Log.e(TAG,"Username update request success")
                    }
                }
            }
        }
    }

    init {
        val user = FirebaseAuth.getInstance().currentUser
        if(user == null) {
            val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        } else {
            Log.e(TAG, "user ${user.displayName} email ${user.email}")
        }
    }
}
