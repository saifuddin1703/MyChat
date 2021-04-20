package com.example.mychat.UI.bottomnavigationfragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.example.mychat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class ProfileFragement : Fragment() {
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
lateinit var coverimage: ImageView
lateinit var profileimage: ImageView
lateinit var editprofile: ImageView
lateinit var editusername: ImageView
lateinit var done: ImageView
lateinit var username: TextView
lateinit var emailorphone: TextView
lateinit var about: TextView
lateinit var friendsnumber: TextView
lateinit var usernameedittext:EditText
lateinit var mAuth: FirebaseAuth
lateinit var user: User
lateinit var progressBar: ProgressBar
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view=inflater.inflate(R.layout.fragment_profile_fragement, container, false)
        coverimage=view.findViewById(R.id.coverimage)
        profileimage=view.findViewById(R.id.imageView6)
        editprofile=view.findViewById(R.id.editimaage)
        editusername=view.findViewById(R.id.editusername)
        done=view.findViewById(R.id.Username_update_button)
        username=view.findViewById(R.id.textView6)
        emailorphone=view.findViewById(R.id.address)
//        about=view.findViewById(R.id.aboutbox)
        usernameedittext=view.findViewById(R.id.usernameedittext)
        friendsnumber=view.findViewById(R.id.friendn)
        progressBar=view.findViewById(R.id.progressBar2)
        progressBar.visibility=View.INVISIBLE
        mAuth= FirebaseAuth.getInstance()
        val userid=mAuth.currentUser?.uid
        val userdao=userdao()

        userdao.getuserbyid(userid!!).addOnSuccessListener {
             user=it.toObject(User::class.java)!!
            user?.photourl?.let {
                Glide.with(requireContext()).load(it).circleCrop().into(profileimage)
            }
            username.text=user?.username
            emailorphone.text=if (mAuth.currentUser?.email!=null) mAuth.currentUser!!.email else mAuth.currentUser?.phoneNumber
            friendsnumber.text=user?.friends?.size.toString()
        }

        done.setOnClickListener {
            val updatedusername= usernameedittext.text.toString()
            done.isClickable=false
            if (updatedusername.isNotEmpty()){
                user.username=updatedusername
                userdao.updateuser(user)
            }else{
                Toast.makeText(requireContext(),"Username cannot be empty",Toast.LENGTH_SHORT).show()
                done.isClickable=true

            }
            done.isClickable=true

        }

        val intent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type="image/*"
        editprofile.setOnClickListener {
            editprofile.isClickable=false
            startActivityForResult(Intent.createChooser(intent,"Select image for profile "),121)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==121){
            //  photourl=data?.data.toString()
            progressBar.visibility=View.VISIBLE
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
            progressBar.visibility=View.INVISIBLE
            editprofile.isClickable=true

            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
        }.addOnCompleteListener {
            if (it.isSuccessful){
                storaageref.downloadUrl.addOnSuccessListener {
                    user.photourl=it.toString()
                    Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
                    Glide.with(requireContext()).load(it).circleCrop().listener(object :RequestListener<Drawable?>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                            editprofile.isClickable=true
                            progressBar.visibility=View.INVISIBLE
                            return true
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            editprofile.isClickable=true
                            progressBar.visibility=View.INVISIBLE
                            return true
                        }

                    }).into(profileimage)
                }
            }
        }

    }
}