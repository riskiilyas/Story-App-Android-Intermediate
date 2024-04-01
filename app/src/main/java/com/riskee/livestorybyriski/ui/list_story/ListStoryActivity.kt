package com.riskee.livestorybyriski.ui.list_story

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.riskee.livestorybyriski.R
import com.riskee.livestorybyriski.databinding.ActivityListStoryBinding
import com.riskee.livestorybyriski.ui.add_story.AddStoryActivity
import com.riskee.livestorybyriski.ui.detail.DetailActivity
import com.riskee.livestorybyriski.ui.main.MainActivity
import com.riskee.livestorybyriski.ui.maps.MapsActivity
import com.riskee.livestorybyriski.utils.LoadingStateAdapter
import com.riskee.livestorybyriski.utils.StoryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private val viewModel: ListStoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = StoryAdapter {
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("idStory", it.id)
            }
            startActivity(intent)
        }

        binding.recyclerViewStories.apply {
            setAdapter(adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            ))
            layoutManager = LinearLayoutManager(this@ListStoryActivity)
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        viewModel.getAllStories.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    viewModel.logout()

                    if (!viewModel.checkLoggedIn()) {
                        val intent =
                            Intent(this, MainActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        startActivity(intent)
                        finish()
                        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Logout Failed! Try Again.", Toast.LENGTH_SHORT).show()
                    }
                    true
                }

                R.id.action_map -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }
}