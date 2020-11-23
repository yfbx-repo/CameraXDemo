package com.phoenix.silentcamera.utils

import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

/**
 * Author: Edward
 * Date: 2020-11-21
 * Description:
 */

/**
 * 保存图片到App私有目录，不需要权限，相册不可见
 */
fun Bitmap.save(file: File): Boolean {
    try {
        val fos = FileOutputStream(file)
        compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
        return true
    } catch (t: Throwable) {
        t.printStackTrace()
    }
    return false
}


/**
 * 保存图片到相册，需要权限
 * [MediaStore.Images.Media.insertImage]
 */
fun Bitmap.save(context: Context): Boolean {
    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
    if (uri != null) {
        val outputStream = context.contentResolver.openOutputStream(uri)
        val isSucceed = compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream?.flush()
        outputStream?.close()
        recycle()
        return isSucceed
    }
    return false
}


fun screenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun screenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}


fun defaultImageFile(): File {
    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val fileName = "${System.currentTimeMillis()}.jpg"
    return File(dir, fileName)
}