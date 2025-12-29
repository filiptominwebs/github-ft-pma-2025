package com.example.eduapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eduapp.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegisterCreate.setOnClickListener {
            val username = binding.etRegUsername.text?.toString()?.trim().orEmpty()
            val email = binding.etRegEmail.text?.toString()?.trim().orEmpty()
            val password = binding.etRegPassword.text?.toString().orEmpty()
            val passwordConfirm = binding.etRegPasswordConfirm.text?.toString().orEmpty()
            binding.tvRegisterError.visibility = View.GONE

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                binding.tvRegisterError.visibility = View.VISIBLE
                binding.tvRegisterError.text = getString(R.string.register_error_fields_empty)
                return@setOnClickListener
            }

            if (password != passwordConfirm) {
                binding.tvRegisterError.visibility = View.VISIBLE
                binding.tvRegisterError.text = getString(R.string.register_error_password_mismatch)
                return@setOnClickListener
            }

            val passwordHash = hashPassword(password)

            lifecycleScope.launch {
                val db = AppDatabase.getInstance(applicationContext)
                val dao = db.userDao()

                val emailExists = withContext(Dispatchers.IO) { dao.countByEmail(email) > 0 }
                if (emailExists) {
                    binding.tvRegisterError.visibility = View.VISIBLE
                    binding.tvRegisterError.text = getString(R.string.register_error_email_taken)
                    return@launch
                }

                val usernameExists = withContext(Dispatchers.IO) { dao.findByUsername(username) != null }
                if (usernameExists) {
                    binding.tvRegisterError.visibility = View.VISIBLE
                    binding.tvRegisterError.text = getString(R.string.register_error_username_taken)
                    return@launch
                }

                // Insert new user and then immediately start HomeActivity (auto-login)
                val newId = withContext(Dispatchers.IO) {
                    dao.insertUser(User(username = username, email = email, passwordHash = passwordHash))
                }

                if (newId > 0) {
                    // start HomeActivity as the logged-in user
                    startActivity(Intent(this@RegisterActivity, HomeActivity::class.java).apply { putExtra("USER_ID", newId) })
                    finish()
                } else {
                    binding.tvRegisterError.visibility = View.VISIBLE
                    binding.tvRegisterError.text = getString(R.string.register_error_fields_empty)
                }
            }
        }
    }

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(password.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }
}
