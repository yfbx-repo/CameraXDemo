package com.phoenix.silentcamera

import android.Manifest
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.yfbx.helper.request
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA) {}

        shotBnt.setOnClickListener {
            takePicture()
        }
    }


    private fun takePicture() {
        val camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
        try {
            val mTexture = SurfaceTexture(0)
            camera.setPreviewTexture(mTexture)
            camera.startPreview()
            camera.takePicture(null, null, this::onPictureTaken)
        } catch (e: Exception) {
            e.printStackTrace()
            camera.release()
        }
    }

    private fun onPictureTaken(bytes: ByteArray?, camera: Camera?) {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val fileName = "${System.currentTimeMillis()}.png"
        val file = File(dir, fileName)
        try {
            val fops = FileOutputStream(file)
            fops.write(bytes)
            fops.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        camera?.release()
    }

}