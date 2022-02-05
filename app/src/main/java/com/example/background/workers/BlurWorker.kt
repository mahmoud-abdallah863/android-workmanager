package com.example.background.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.R

class BlurWorker (
    context: Context,
    params: WorkerParameters
): Worker(context, params) {

    private val TAG = "BlurWorkerTag"

    override fun doWork(): Result {
        val appContext = applicationContext
        makeStatusNotification("Testing notification", appContext)

        return try {
            val picture: Bitmap = BitmapFactory.decodeResource(
                appContext.resources,
                R.drawable.android_cupcake
            )
            val blurBitmap = blurBitmap(picture, appContext)
            val localUri: Uri = writeBitmapToFile(appContext, blurBitmap)
            makeStatusNotification("Location: $localUri", appContext)

            Result.success()
        }catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            Result.failure()
        }

    }

}