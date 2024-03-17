package com.example.swiftconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.swiftconnect.Model.User
import com.example.swiftconnect.databinding.ActivitySignUpBinding
import com.example.swiftconnect.utils.USER_NODE
import com.example.swiftconnect.utils.USER_PROFILE_FOLDER
import com.example.swiftconnect.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var user: User

    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()){uri->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER){
                if(it==null){
                    Toast.makeText(this, "Cannot Select Image", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                    user.image = it
                    binding.profileImg.setImageURI(uri)
                }
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = User()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerBtn.setOnClickListener {
            if(binding.nameEdt.text.toString().equals("") || binding.emailEdt.text.toString().equals("") || binding.passEdt.text.toString().equals("")){
                Toast.makeText(this, "Please Fill All the Details", Toast.LENGTH_SHORT).show()
            }else{
                val email = binding.emailEdt.text.toString()
                val password = binding.passEdt.text.toString()
                val name = binding.nameEdt.text.toString()

                registerUser(email, password, name)
            }
        }

        binding.profileImg.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.LoginTxt.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser(email: String, password: String, name: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {result->
                if(result.isSuccessful){
                    user.name = name
                    user.email = email
                    user.password = password

                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                }
                else{
                    Toast.makeText(this, "${result.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }

            }
    }
}