package com.example.dailycaloriestracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.dailycaloriestracker.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val userOrEmail = binding.etUsername.text?.toString()?.trim().orEmpty()
            val password = binding.etPassword.text?.toString().orEmpty()
            binding.tvLoginError.visibility = View.GONE

            if (userOrEmail.isEmpty() || password.isEmpty()) {
                binding.tvLoginError.visibility = View.VISIBLE
                binding.tvLoginError.text = getString(R.string.login_error_empty)
                return@setOnClickListener
            }

            val hashed = hashPassword(password)

            lifecycleScope.launch {
                val db = AppDatabase.getInstance(applicationContext)
                val dao = db.userDao()
                val user = withContext(Dispatchers.IO) {
                    dao.authenticateByEmail(userOrEmail, hashed) ?: dao.authenticateByUsername(userOrEmail, hashed)
                }

                if (user != null) {
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java).apply {
                        putExtra("USER_ID", user.id)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    binding.tvLoginError.visibility = View.VISIBLE
                    binding.tvLoginError.text = getString(R.string.login_error_bad)
                }
            }
        }

        binding.btnGoToRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(password.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }
}