package com.riskee.livestorybyriski.ui.detail

import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.riskee.livestorybyriski.R
import com.riskee.livestorybyriski.databinding.ActivityDetailBinding
import com.riskee.livestorybyriski.utils.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idStory = intent.getStringExtra("idStory")

        lifecycleScope.launch {
            viewModel.storyState.collect {
                when (it) {
                    is Resource.LOADING -> {
                        binding.btnRefresh.visibility = View.GONE
                        Toast.makeText(
                            this@DetailActivity,
                            "Fetching the stories...",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    is Resource.SUCCESS -> {
                        binding.btnRefresh.visibility = View.GONE
                        binding.tvDetailName.text = getString(R.string.name, it.result.name)
                        binding.textViewCreatedAt.text =
                            getString(R.string.created_at, it.result.createdAt)
                        binding.textViewLatLon.text =
                            getString(R.string.location, getCityName(it.result.lat, it.result.lon))
                        binding.tvDetailDescription.text = it.result.description

                        Glide.with(this@DetailActivity).load(it.result.photoUrl)
                            .into(binding.ivDetailPhoto)
                    }

                    is Resource.ERROR -> {
                        binding.btnRefresh.visibility = View.VISIBLE
                        Toast.makeText(
                            this@DetailActivity,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }


        binding.btnRefresh.setOnClickListener {
            viewModel.getDetailStory(idStory.toString())
        }
        viewModel.getDetailStory(idStory.toString())
    }

    private fun getCityName(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return try {
            addresses?.get(0)!!.locality ?: ""
        } catch (e: Exception) {
            "Unknown"
        }
    }
}