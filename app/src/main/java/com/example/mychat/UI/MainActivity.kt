package com.example.mychat.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.mychat.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    lateinit var googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 121
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val motionLayout:MotionLayout=findViewById(R.id.motionlayout)
        supportActionBar?.hide()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        //  val tv:TextView=navHostFragment.targetFragment

}


}