package com.example.handsonchatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.handsonchatapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginButtonLogin.setOnClickListener {
            val email = binding.emailEdittextLogin.text.toString()
            val password = binding.passwordEdittextLogin.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter text in email or password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d(TAG, "email : ${email}, password:${password}")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Toast.makeText(this, "Failed to Login", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }

                    Log.d(TAG, "Successful Login")
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to Login", Toast.LENGTH_SHORT).show()
                }
        }

        binding.backToRegisterTextLogin.setOnClickListener {
            finish()
        }
    }
}