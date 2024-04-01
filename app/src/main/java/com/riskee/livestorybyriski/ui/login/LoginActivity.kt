package com.riskee.livestorybyriski.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.riskee.livestorybyriski.databinding.ActivityLoginBinding
import com.riskee.livestorybyriski.ui.add_story.AddStoryActivity
import com.riskee.livestorybyriski.ui.list_story.ListStoryActivity
import com.riskee.livestorybyriski.ui.register.RegisterActivity
import com.riskee.livestorybyriski.utils.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            if (binding.edLoginEmail.isValidEmail() && binding.edLoginPassword.isValidPassword()) {
                switchButtons(false)
                viewModel.login(
                    binding.edLoginEmail.text.toString(),
                    binding.edLoginPassword.text.toString()
                )
            } else {
                Toast.makeText(this, "Fill Email & Password First!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnloginAsGuest.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            intent.putExtra("isGuest", true)
            startActivity(intent)
        }


        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        playAnimation()

        lifecycleScope.launch {
            viewModel.loginState.collect {
                when (it) {
                    is Resource.ERROR -> {
                        switchButtons(true)
                        Toast.makeText(
                            this@LoginActivity,
                            "Login Failed! Please Check Your Connection",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    is Resource.SUCCESS -> {
                        switchButtons(true)
                        Toast.makeText(
                            this@LoginActivity,
                            "Successfully Logged In!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent =
                            Intent(this@LoginActivity, ListStoryActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        startActivity(intent)
                        finish()
                    }

                    is Resource.LOADING -> {
                        switchButtons(false)
                        Toast.makeText(this@LoginActivity, "Login...", Toast.LENGTH_SHORT).show()

                    }

                    else -> {}
                }
            }
        }
    }

    private fun switchButtons(enabled: Boolean) {
        binding.buttonLogin.isEnabled = enabled
        binding.buttonRegister.isEnabled = enabled
        binding.btnloginAsGuest.isEnabled = enabled
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageViewLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val login = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1f).setDuration(200)
        val signup = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(200)
        val guest = ObjectAnimator.ofFloat(binding.btnloginAsGuest, View.ALPHA, 1f).setDuration(200)
        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(200)
        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(200)
        val pass = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(200)
        AnimatorSet().apply {
            playSequentially(title, email, pass, login, signup, guest)
            start()
        }
    }
}

