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
import java.lang.IllegalArgumentException

class BlurWorker (
    context: Context,
    params: WorkerParameters
): Worker(context, params) {

    private val TAG = "BlurWorkerTag"

    override fun doWork(): Result {
        val appContext = applicationContext
        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Testing notification", appContext)

        return try {

            if (TextUtils.isEmpty(resourceUri)) {
                Log.d(TAG, "doWork: Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver
            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
            )

            val blurBitmap = blurBitmap(picture, appContext)
            val outputUri: Uri = writeBitmapToFile(appContext, blurBitmap)

            makeStatusNotification("Location: $outputUri", appContext)

            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            Result.success(outputData)

        }catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            Result.failure()
        }

    }

}