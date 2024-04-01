package com.riskee.livestorybyriski.ui.add_story

import android.Manifest
import android.Manifest.permission.CAMERA
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.riskee.livestorybyriski.BuildConfig
import com.riskee.livestorybyriski.databinding.ActivityAddStoryBinding
import com.riskee.livestorybyriski.ui.list_story.ListStoryActivity
import com.riskee.livestorybyriski.utils.Resource
import com.riskee.livestorybyriski.utils.reduceFileImage
import com.riskee.livestorybyriski.utils.uriToFile
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddStoryActivity : AppCompatActivity() {
    companion object {
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    }

    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel: AddStoryViewModel by viewModel()
    private var currentImageUri: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isGrantedLocation = false
    private var lat: Double? = null
    private var lon: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isGuest = intent.getBooleanExtra("isGuest", false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.cameraButton.setOnClickListener {
            startCamera()
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.buttonAdd.setOnClickListener {
            if (currentImageUri != null && binding.edAddDescription.text.toString().isNotEmpty()) {
                val imageFile = uriToFile(currentImageUri!!, this).reduceFileImage()
                if (binding.cbLocation.isChecked) {
                    requestLocationUpdates {
                        if (isGuest) {
                            viewModel.addNewStoryGuest(
                                binding.edAddDescription.text.toString(),
                                imageFile,
                                lat.toString(),
                                lon.toString()
                            )
                        } else {
                            viewModel.addNewStory(
                                binding.edAddDescription.text.toString(),
                                imageFile,
                                lat.toString(),
                                lon.toString()
                            )
                        }
                    }
                } else {
                    if (isGuest) {
                        viewModel.addNewStoryGuest(
                            binding.edAddDescription.text.toString(),
                            imageFile
                        )
                    } else {
                        viewModel.addNewStory(
                            binding.edAddDescription.text.toString(),
                            imageFile
                        )
                    }
                }
            } else {
                Toast.makeText(this, "Image or description is empty", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.addStoryState.collect {
                when (it) {
                    is Resource.LOADING -> {
                        Toast.makeText(
                            this@AddStoryActivity,
                            "Fetching the stories...",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.buttonAdd.isEnabled = false
                    }

                    is Resource.SUCCESS -> {
                        Toast.makeText(
                            this@AddStoryActivity,
                            "Success add new story!",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.buttonAdd.isEnabled = true


                        if (!isGuest) {
                            val intent =
                                Intent(this@AddStoryActivity, ListStoryActivity::class.java).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                            startActivity(intent)
                            finish()
                        }
                    }

                    is Resource.ERROR -> {
                        binding.buttonAdd.isEnabled = true

                        Toast.makeText(
                            this@AddStoryActivity,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }

        getLocationPermission()
    }

    private fun getLocationPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            isGrantedLocation = if (!isGranted) {
                Toast.makeText(this, "Location Permission Required", Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }

        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun requestLocationUpdates(onResult: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lat = location.latitude
                lon = location.longitude
            }
            onResult.invoke()
        }
    }

    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

    private fun getImageUri(context: Context): Uri {
        var uri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
            }
            uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        }
        return uri ?: getImageUriForPreQ(context)
    }

    private fun getImageUriForPreQ(context: Context): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
        if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            imageFile
        )
    }

    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA), 100)
        } else {
            currentImageUri = getImageUri(this)
            launcherIntentCamera.launch(currentImageUri)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentImageUri = getImageUri(this)
                launcherIntentCamera.launch(currentImageUri)
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imageView.setImageURI(it)
        }
    }
}