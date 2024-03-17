package com.example.swiftconnect.Post

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.swiftconnect.HomeActivity
import com.example.swiftconnect.Model.Post
import com.example.swiftconnect.Model.Reel
import com.example.swiftconnect.Model.User
import com.example.swiftconnect.R
import com.example.swiftconnect.databinding.ActivityReelsBinding
import com.example.swiftconnect.utils.POST
import com.example.swiftconnect.utils.POST_FOLDER
import com.example.swiftconnect.utils.REEL
import com.example.swiftconnect.utils.REEL_FOLDER
import com.example.swiftconnect.utils.USER_NODE
import com.example.swiftconnect.utils.uploadImage
import com.example.swiftconnect.utils.uploadVideo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ReelsActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityReelsBinding.inflate(layoutInflater)
    }
    private lateinit var videoUrl: String
    lateinit var progressDialog: ProgressDialog
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        uri?.let {
            uploadVideo(uri, REEL_FOLDER,progressDialog){
                if(it==null){
                    Toast.makeText(this, "Cannot Select Image", Toast.LENGTH_SHORT).show()
                }
                else{
                    videoUrl = it
                }
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.postReel.setOnClickListener {
            launcher.launch("video/*")
        }
        binding.cancelBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }


        binding.postBtn.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                var user:User = it.toObject<User>()!!
                var reel = Reel(videoUrl,binding.edtCaption.text.toString(),user.image!!)
                Firebase.firestore.collection(REEL).document().set(reel)
                    .addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ REEL).document().set(reel)
                            .addOnSuccessListener {
                                startActivity(Intent(this,HomeActivity::class.java))
                                finish()
                            }

                    }
            }
        }

    }
}