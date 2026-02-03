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


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val db = AppDatabase.getInstance(this)

        lifecycleScope.launch(Dispatchers.IO) {
            db.userDao().clearUsers()

            db.userDao().insertUser(
                User(
                    name = "Test User",
                    email = "test@gsu.edu",
                    passwordHash = "123"
                )
            )

            val found = db.userDao().getUserByEmail("test@gsu.edu")
            Log.d("ROOM_TEST", "FOUND USER: $found")
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

