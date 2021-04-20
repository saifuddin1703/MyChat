package com.example.mychat.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.*


class SignupFragment : Fragment() {
    lateinit var googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN=121
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController=this.findNavController()
        // Inflate the layout for this fragment
      val view=inflater.inflate(R.layout.fragment_signup, container, false)
        val user=FirebaseAuth.getInstance().currentUser
        updateUI(user)
        val motionLayout: MotionLayout =view.findViewById(R.id.motionlayout)
     //   context.supportActionBar?.hide()
        mAuth= FirebaseAuth.getInstance()
        GlobalScope.launch {
            delay(3000)
            withContext(Dispatchers.Main){
                motionLayout.transitionToEnd()
            }
        }

        val googlebutton: MaterialButton =view.findViewById(R.id.materialButton)
        val phonebutton: MaterialButton =view.findViewById(R.id.materialButton2)
        phonebutton.setOnClickListener {
            navController.navigate(R.id.action_SignupFragment_to_signupwithphonefragment)
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        googlebutton.setOnClickListener {
            signIn()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val user=FirebaseAuth.getInstance().currentUser
        updateUI(user)
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
             //   Toast.makeText(this, "signin 2", Toast.LENGTH_SHORT)
                val account = task.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        activity?.let {
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(
                    it
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success")
                        val user: FirebaseUser? = mAuth.currentUser
                            val userdao=userdao()

                        Toast.makeText(context, "signin successful", Toast.LENGTH_SHORT).show()
                         userdao.isUserExists(user?.uid!!).addOnCompleteListener {
                            val task= it.result
                            task?.let {
                                if(it.exists())
                                    navController.navigate(R.id.action_SignupFragment_to_homepageFragment2)
                                else
                                    navController.navigate(R.id.action_SignupFragment_to_profileSetupFragment)
                            }

                        }

                      //  updateUI(user)
                    } else {
                        Toast.makeText(context, "signin failed", Toast.LENGTH_SHORT).show()
                    }

                    // ...
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        user?.let {
            navController.navigate(R.id.action_SignupFragment_to_homepageFragment2)
        }

    }

}