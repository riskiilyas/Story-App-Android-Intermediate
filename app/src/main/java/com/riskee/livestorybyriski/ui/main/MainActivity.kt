package com.riskee.livestorybyriski.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.riskee.livestorybyriski.databinding.ActivityMainBinding
import com.riskee.livestorybyriski.ui.list_story.ListStoryActivity
import com.riskee.livestorybyriski.ui.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.loginState.collect {
                delay(2000)
                if (it == true) {
                    val intent =
                        Intent(this@MainActivity, ListStoryActivity::class.java).apply {
                            flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    startActivity(intent)
                    finish()
                } else if (it == false) {
                    val intent =
                        Intent(this@MainActivity, LoginActivity::class.java).apply {
                            flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    startActivity(intent)
                    finish()
                }
            }
        }

        viewModel.checkLoggedIn()
    }
}