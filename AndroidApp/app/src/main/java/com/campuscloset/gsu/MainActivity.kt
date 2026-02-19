package com.campuscloset.gsu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.campuscloset.gsu.network.SupabaseClient
import com.campuscloset.gsu.network.SupabaseUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val repo by lazy { SupabaseUserRepository(SupabaseClient.api) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            getSharedPreferences("session", MODE_PRIVATE).edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val userId = prefs.getInt("userId", -1)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val user = repo.getUserById(userId)

                withContext(Dispatchers.Main) {
                    tvWelcome.text = if (user != null) "Welcome, ${user.name}" else "Welcome!"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { tvWelcome.text = "Welcome!" }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
