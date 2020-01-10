package com.flover.ocrapplication

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.flover.ocrapplication.util.PostMan
import com.theartofdev.edmodo.cropper.CropImage


class MainActivity : AppCompatActivity() {
    private lateinit var textView : TextView
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        findViewById<ImageView>(R.id.uploaded_image).setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 1)

        }

        findViewById<Button>(R.id.show_results_btn).setOnClickListener {
            var intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.upload_result_btn).setOnClickListener {
            progressDialog.show()
            PostMan.postData(progressDialog, this)
        }

        findViewById<Button>(R.id.camera_btn).setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 2)
        }

        textView = findViewById(R.id.show_result)
        textView.text = ""
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("LOADING!")
        progressDialog.setMessage("WAITING FOR SERVER RESPONSE!")
        progressDialog.setCanceledOnTouchOutside(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==1&&resultCode== Activity.RESULT_OK&&data!=null){
            progressDialog.show()
            var uri: Uri? = data.data
            findViewById<ImageView>(R.id.uploaded_image).setImageURI(uri)

            uri?.let { PostMan.getImageResult(it, this, progressDialog) }

        }else if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode== Activity.RESULT_OK&&data!=null){

            progressDialog.show()
            var result = CropImage.getActivityResult(data)
            var uri = result.uri

            findViewById<ImageView>(R.id.uploaded_image).setImageURI(uri)
            PostMan.getImageResult(uri, this, progressDialog)
        }else if(requestCode==2&&resultCode== Activity.RESULT_OK&&data!=null){
            CropImage.activity(data?.data)
                .start(this)
        }
    }
}