package com.phoenix.silentcamera

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import com.phoenix.silentcamera.utils.capture
import com.phoenix.silentcamera.utils.defaultImageFile
import com.phoenix.silentcamera.utils.startCamera
import com.yfbx.helper.request
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private val imageCapture by lazy { getDefaultCapture() }
    private lateinit var cameraExecutor: ExecutorService
    private var front = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA) {
        }
        cameraExecutor = Executors.newSingleThreadExecutor()

        openCamera()

        previewView.setOnClickListener {
            imageCapture.capture(this, defaultImageFile())
        }

        previewView.setOnLongClickListener {
            switchCamera()
            true
        }
    }

    private fun getDefaultCapture(): ImageCapture {
        val builder = ImageCapture.Builder()
        builder.setFlashMode(ImageCapture.FLASH_MODE_OFF)
        builder.setTargetAspectRatio(AspectRatio.RATIO_16_9)
        builder.setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
        return builder.build()
    }

    private fun openCamera() {
        val selector = if (front) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
        previewView.startCamera(this, selector, imageCapture)
    }

    private fun switchCamera() {
        front = !front
        openCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}