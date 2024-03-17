package com.example.swiftconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.swiftconnect.Model.User
import com.example.swiftconnect.databinding.ActivityLoginBinding
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEdt.text.toString()
            val password = binding.passEdt.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_SHORT).show()
            }
            else{
                val user = User(email,password)
                loginWithEmailAndPass(user)
            }
        }

        binding.textView.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun loginWithEmailAndPass(user: User) {
        Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }


}