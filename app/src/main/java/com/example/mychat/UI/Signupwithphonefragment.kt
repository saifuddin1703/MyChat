package com.example.mychat.UI

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.hbb20.CCPCountry
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit

class Signupwithphonefragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    lateinit var navController: NavController
  lateinit var auth: FirebaseAuth
    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController=this.findNavController()

        auth= FirebaseAuth.getInstance()
        val view=inflater.inflate(R.layout.fragment_signupwithphonefragment, container, false)
        progressBar=view.findViewById(R.id.progressBar)
        val tv=view.findViewById<TextView>(R.id.phonenumber)
        val done=view.findViewById<MaterialButton>(R.id.donephone)
        val ccp: CountryCodePicker =view.findViewById(R.id.country_code_picker)
        var phonenumber= ccp.selectedCountryCodeWithPlus

        done.setOnClickListener {
            if (tv.text.length==10) {
                phonenumber += tv.text.trim()
                progressBar.visibility=View.VISIBLE
                signupwithphone(phonenumber)
            }else Toast.makeText(context,"Invalid phonenumber",Toast.LENGTH_SHORT).show()

        }


        return view
    }

    private fun signupwithphone(phonenumber: String) {


        val options= PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phonenumber)
            .setActivity(requireActivity())
            .setTimeout(60L,TimeUnit.SECONDS)
            .setCallbacks(object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                   // Toast.makeText(context,"Signin succesful",Toast.LENGTH_SHORT).show()
                    signInWithPhoneAuthCredential(p0)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(context,"Signin unsuccesful",Toast.LENGTH_SHORT).show()
                }
                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d(TAG, "onCodeSent:$verificationId")

                    // Save verification ID and resending token so we can use them later
//            storedVerificationId = verificationId
//            resendToken = token
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                         Toast.makeText(context,"Signin succesful",Toast.LENGTH_SHORT).show()
                    progressBar.visibility=View.INVISIBLE
                    val user = task.result?.user
                    val userdao=userdao()
                    userdao.isUserExists(user?.uid!!).addOnCompleteListener {
                        val task= it.result
                        task?.let {
                            if(it.exists())
                                navController.navigate(R.id.action_signupwithphonefragment_to_homepageFragment)
                            else
                                navController.navigate(R.id.action_signupwithphonefragment_to_profileSetupFragment)
                        }

                    }
                  //  navController.navigate(R.id.action_signupwithphonefragment_to_profileSetupFragment)

                } else {

                   // Toast.makeText(context,"Signin failed",Toast.LENGTH_SHORT).show()
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                   //     Toast.makeText(context,"Signin unsuccesful",Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }











//   val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//
//            Log.d(TAG, "onVerificationCompleted:$credential")
////            signInWithPhoneAuthCredential(credential)
//        }
//
//
//       override fun onVerificationFailed(e: FirebaseException) {
//            // This callback is invoked in an invalid request for verification is made,
//            // for instance if the the phone number format is not valid.
//            Log.w(TAG, "onVerificationFailed", e)
//
//            if (e is FirebaseAuthInvalidCredentialsException) {
//                // Invalid request
//            } else if (e is FirebaseTooManyRequestsException) {
//                // The SMS quota for the project has been exceeded
//            }
//
//            // Show a message and update the UI
//        }
//
//        override fun onCodeSent(
//            verificationId: String,
//            token: PhoneAuthProvider.ForceResendingToken
//        ) {
//            // The SMS verification code has been sent to the provided phone number, we
//            // now need to ask the user to enter the code and then construct a credential
//            // by combining the code with a verification ID.
//            Log.d(TAG, "onCodeSent:$verificationId")
//
//            // Save verification ID and resending token so we can use them later
////            storedVerificationId = verificationId
////            resendToken = token
//        }
//    }


}