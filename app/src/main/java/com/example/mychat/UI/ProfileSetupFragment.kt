package com.example.mychat.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.example.mychat.model.FriendRequest
import com.example.mychat.model.User
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProfileSetupFragment : Fragment() {

    lateinit var profileimage:ImageView
    lateinit var changeimage:ImageView
    lateinit var firstnametv:TextView
    lateinit var lastnametv:TextView
    lateinit var done:MaterialButton
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
     var photourl:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navController=this.findNavController()
        val view= inflater.inflate(R.layout.fragment_profile_setup, container, false)
        profileimage=view.findViewById(R.id.imageView2)
        changeimage=view.findViewById(R.id.imageView3)
        firstnametv=view.findViewById(R.id.firstname)
        lastnametv=view.findViewById(R.id.lastname)
        done=view.findViewById(R.id.done)

        done.setOnClickListener {
            val fname=firstnametv.text.toString()
            val lname=lastnametv.text.toString()
            val uid=FirebaseAuth.getInstance().currentUser?.uid
            val userdao=userdao()
            if (fname.isNotEmpty() && lname.isNotEmpty()){
                val name= "$fname $lname"
                FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnSuccessListener {
                    val token=it.token
                    val user=User(name,uid!!,photourl,ArrayList<String>(),ArrayList<FriendRequest>(),ArrayList<FriendRequest>(),token)
                    userdao.adduser(user)

                    navController.navigate(R.id.action_profileSetupFragment_to_homepageFragment)
                }

            }else Toast.makeText(context,"Name field cannot be empty",Toast.LENGTH_SHORT).show()
        }


     changeimage.setOnClickListener {
         val intent = Intent(
             Intent.ACTION_PICK,
             MediaStore.Images.Media.EXTERNAL_CONTENT_URI
         )
         intent.type = "image/*"
         //  intent.action = Intent.ACTION_GET_CONTENT
         startActivityForResult(Intent.createChooser(intent, "Select Picture"), 121)
     }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==121){
          //  photourl=data?.data.toString()
          uploadimage(data?.data)
        }
    }

    private fun uploadimage(data: Uri?) {
        var file = data!!//Uri.fromFile(File(data.toString()))
       val storaageref= FirebaseStorage.getInstance().getReference().child("displaypictures/${System.currentTimeMillis()}.jpg")

        storaageref.putFile(file).addOnProgressListener {
            val progress = it.bytesTransferred/it.totalByteCount*100
            Toast.makeText(requireContext(),"$progress",Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
        }.addOnCompleteListener {
            if (it.isSuccessful){
                storaageref.downloadUrl.addOnSuccessListener {
                    photourl=it.toString()
                    Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
                    Glide.with(requireContext()).load(it).circleCrop().into(profileimage)
                }
            }
        }

    }

}