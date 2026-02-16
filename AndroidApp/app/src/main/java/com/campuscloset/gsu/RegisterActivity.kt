package com.campuscloset.gsu

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.campuscloset.gsu.data.AppDatabase
import com.campuscloset.gsu.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnCreate = findViewById<Button>(R.id.btnCreateAccount)

        val db = AppDatabase.getInstance(this)

        btnCreate.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.endsWith("@student.gsu.edu")) {
                Toast.makeText(this, "Must use your student.gsu.edu email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            lifecycleScope.launch(Dispatchers.IO) {
                val existing = db.userDao().getUserByEmail(email)

                if (existing != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, "Email already exists", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    db.userDao().insertUser(
                        User(
                            name = name,
                            email = email,
                            passwordHash = password // (we can hash later)
                        )
                    )

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, "Account created!", Toast.LENGTH_SHORT).show()
                        finish() // goes back to Login
                    }
                }
            }
        }
    }
}
