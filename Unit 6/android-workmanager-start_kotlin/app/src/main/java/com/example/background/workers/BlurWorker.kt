package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.TAG_OUTPUT

private const val TAG = "Blur Worker"
class BlurWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {
        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring image", appContext)
        sleep()

        return try {
            if(TextUtils.isEmpty(resourceUri)){
                Log.e(TAG, "Invalid input Uri")
                throw IllegalArgumentException("Invalid input Uri")
            }
            val resolver = appContext.contentResolver
            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
            )

            val output = blurBitmap(picture, appContext)

            val blurredImageUri = writeBitmapToFile(appContext, output)

            makeStatusNotification(blurredImageUri.toString(), appContext)
            val outputData = workDataOf(KEY_IMAGE_URI to blurredImageUri.toString())
            return Result.success(outputData)
        } catch(e: Throwable) {
            Log.e(TAG, "Error applying blur")
            e.printStackTrace()
            return Result.failure()
        }

    }
}