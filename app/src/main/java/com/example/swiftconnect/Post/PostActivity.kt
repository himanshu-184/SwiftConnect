package com.example.swiftconnect.Post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.swiftconnect.HomeActivity
import com.example.swiftconnect.Model.Post
import com.example.swiftconnect.Model.User
import com.example.swiftconnect.R
import com.example.swiftconnect.databinding.ActivityPostBinding
import com.example.swiftconnect.utils.POST
import com.example.swiftconnect.utils.POST_FOLDER
import com.example.swiftconnect.utils.USER_NODE
import com.example.swiftconnect.utils.USER_PROFILE_FOLDER
import com.example.swiftconnect.utils.generateRandomId
import com.example.swiftconnect.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    private var imageUrl:String?=null
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        uri?.let {
            uploadImage(uri, POST_FOLDER){
                if(it==null){
                    Toast.makeText(this, "Cannot Select Image", Toast.LENGTH_SHORT).show()
                }
                else{
                    binding.postImg.setImageURI(uri)
                    imageUrl = it
                }
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.postImg.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.cancelBtn.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
        binding.postBtn.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

                val user = it.toObject<User>()
                val postId = generateRandomId(12)

                var post = Post(postId,
                    imageUrl!!,
                    binding.edtCaption.text.toString(),
                    Firebase.auth.currentUser!!.uid,
                    System.currentTimeMillis().toString(),0
                )

                Firebase.firestore.collection(POST).document(postId).set(post)
                    .addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post)
                            .addOnSuccessListener {
                                startActivity(Intent(this,HomeActivity::class.java))
                                finish()
                            }

                    }
            }
        }
    }
}