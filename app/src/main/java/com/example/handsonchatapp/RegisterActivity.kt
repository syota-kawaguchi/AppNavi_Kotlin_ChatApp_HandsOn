package com.example.handsonchatapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.handsonchatapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.registerButtonRegister.setOnClickListener {
              performRegister()
       }

        binding.haveAccountTextRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            Log.d(TAG, "try to show login activity")
        }

        binding.selectPhotoButtonRegister.setOnClickListener {
            Log.d(TAG, "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    private fun performRegister() {
        val email = binding.emailEdittextRegister.text.toString();
        val password = binding.passwordEdittextRegister.text.toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email or password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Email is: ${email}")
        Log.d(TAG, "password is: ${password}")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (!it.isSuccessful) {
                    Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }

                //else if successful
                Log.d(TAG, "Successfully created user with uid: ${it.result.user?.uid}")
                val intent = Intent(this, MessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener{
                //emailのformatが違ったら実行
                Log.d(TAG, "failed to create user message ${it.message}")
                Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d(TAG, "Photo was selected")

            val uri = data.data

            //APIレベルによってbitmapの取得方法の推奨が違う
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            binding.circleViewRegister.setImageBitmap(bitmap)
            binding.selectPhotoButtonRegister.alpha = 0f
        }
    }
}