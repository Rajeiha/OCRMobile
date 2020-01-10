package com.flover.ocrapplication.util

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import com.flover.ocrapplication.data.OCRData
import com.flover.ocrapplication.R
import com.loopj.android.http.RequestParams
import com.loopj.android.http.SyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.io.File

object PostMan {
    private lateinit var path: String
    private lateinit var string: String
    fun getImageResult(uri: Uri, activity: Activity, progressDialog: ProgressDialog){
        var textView: TextView = activity.findViewById(R.id.show_result)
        var client = SyncHttpClient()
        var params = RequestParams()

        path = uri?.let { PathGenerator.getPath(activity, it) }!!

        params.put("image", File(path), path?.let { PathGenerator.getContentType(it) })
        // params.put("result", "GOOGLE!")
        client.connectTimeout = 60000
        client.responseTimeout = 120000
        Thread{
            Looper.prepare()
            client.post(OCRData.ocrPostUrl, params, object : TextHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?
                ) {
                    val json = JSONObject(responseString)
                    string = json["image_text"].toString()
                    activity.runOnUiThread {
                        textView.text = string
                        progressDialog.dismiss()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?
                ) {
                    progressDialog.dismiss()
                    Toast.makeText(activity, "SERVER DOWN!", Toast.LENGTH_SHORT).show()
                }
            })
            Looper.loop()
        }.start()
    }

    fun postData(progressDialog: ProgressDialog, activity: Activity){
        var client = SyncHttpClient()
        var params = RequestParams()

        try {
            params.put("image", File(path), path?.let { PathGenerator.getContentType(it) })
            params.put("result", string)
            Thread{
                Looper.prepare()
                client.post(OCRData.uploadPostUrl, params, object : TextHttpResponseHandler(){
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseString: String?
                    ) {
                        println(responseString)
                        progressDialog.dismiss()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseString: String?,
                        throwable: Throwable?
                    ) {
                        progressDialog.dismiss()
                        Toast.makeText(activity, "SERVER DOWN!", Toast.LENGTH_SHORT).show()
                    }
                })
                Looper.loop()
            }.start()
        }catch (e: UninitializedPropertyAccessException){
            Toast.makeText(activity, "EMPTY DATA!", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }
    }
}