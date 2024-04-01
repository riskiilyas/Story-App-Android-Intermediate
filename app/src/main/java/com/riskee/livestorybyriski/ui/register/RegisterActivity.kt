package com.riskee.livestorybyriski.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.riskee.livestorybyriski.databinding.ActivityRegisterBinding
import com.riskee.livestorybyriski.utils.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val isNameValid = binding.edRegisterName.text.toString().isNotEmpty()
            val isEmailValid = binding.edRegisterEmail.isValidEmail()
            val isPasswordValid = binding.edRegisterPassword.isValidPassword()

            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (isNameValid && isEmailValid && isPasswordValid) {
                viewModel.register(name, email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.registerState.collect {
                when (it) {
                    is Resource.LOADING -> {
                        binding.btnRegister.isEnabled = false
                        Toast.makeText(this@RegisterActivity, "Loading...", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resource.SUCCESS -> {
                        binding.btnRegister.isEnabled = true
                        Toast.makeText(
                            this@RegisterActivity,
                            "Register success!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    is Resource.ERROR -> {
                        binding.btnRegister.isEnabled = true
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error! Make sure data is valid",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }
    }
}