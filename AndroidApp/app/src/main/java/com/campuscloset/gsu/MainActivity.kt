package com.campuscloset.gsu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.campuscloset.gsu.data.AppDatabase
import com.campuscloset.gsu.data.User
import android.content.Intent
import android.widget.Button
import android.widget.TextView





class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnLogout = findViewById<Button>(R.id.btnLogout)


        btnLogout.setOnClickListener {
            getSharedPreferences("session", MODE_PRIVATE)
                .edit()
                .clear()
                .apply()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val db = AppDatabase.getInstance(this)


        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val userId = prefs.getInt("userId", -1)

        lifecycleScope.launch {
            val user = db.userDao().getUserById(userId)
            if (user != null) {
                tvWelcome.text = "Welcome, ${user.name}"
            }
        }

        btnLogout.setOnClickListener {
            getSharedPreferences("session", MODE_PRIVATE)
                .edit()
                .clear()
                .apply()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}


