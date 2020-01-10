package com.flover.ocrapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flover.ocrapplication.adapter.RecyclerViewAdapter
import com.flover.ocrapplication.data.OCRData
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import org.json.JSONArray

class ResultsActivity : AppCompatActivity() {

    private val localhost : String = OCRData.localhost

    private var imageUrl  : ArrayList<String> = ArrayList()
    private var imageResult  : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        getHttp()


        findViewById<Button>(R.id.take_pic_btn).setOnClickListener {
            finish()
        }
    }

    private fun getHttp(){
        val httpAsync = OCRData.getUrl
            .httpGet().responseString{_, _, result ->
                when(result){
                    is Result.Failure -> {
                        this@ResultsActivity.runOnUiThread{
                            Toast.makeText(this, "SERVER DOWN!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Result.Success -> {
                        val data = result.get()
                        // println(data)


                        val json = JSONArray(data)

                        for(i in 0 until json.length()){
                            val result = json.getJSONObject(i)
                            imageUrl.add(result.getString("image_path").replace("localhost", localhost))
                            imageResult.add(result.getString("image_text"))
                        }

                        var recyclerView = findViewById<RecyclerView>(R.id.results_recycler_view)
                        var adapter = RecyclerViewAdapter(imageUrl, imageResult, this)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this)
                    }
                }
            }
        httpAsync.join()
    }
}