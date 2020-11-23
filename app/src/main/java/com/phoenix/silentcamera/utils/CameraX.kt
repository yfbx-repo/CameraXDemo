package com.phoenix.silentcamera.utils

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.io.File

/**
 * Author: Edward
 * Date: 2020-11-23
 * Description:
 */

fun PreviewView.startCamera(lifecycleOwner: LifecycleOwner, cameraSelector: CameraSelector, imageCapture: ImageCapture) {
    context.getCameraProvider { cameraProvider ->
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(surfaceProvider)
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
    }
}

fun Context.getCameraProvider(builder: (ProcessCameraProvider) -> Unit): ListenableFuture<ProcessCameraProvider> {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    cameraProviderFuture.addListener({
        builder.invoke(cameraProviderFuture.get())
    }, ContextCompat.getMainExecutor(this))
    return cameraProviderFuture
}


fun ImageCapture.capture(context: Context, photoFile: File) {
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    takePicture(
            outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
        override fun onError(exc: ImageCaptureException) {
            println("Photo capture failed: ${exc.message}")
        }

        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(photoFile)
            println("Photo capture succeeded: $savedUri")
        }
    })
}