package com.flover.ocrapplication.data

object OCRData {
    const val localhost: String = "192.168.43.53"
    /// const val localhost: String = "172.19.51.76"
    const val uploadPostUrl: String = "http://$localhost:3000/api/results"
    const val getUrl: String = "http://$localhost:3000/api/image_results"
    const val ocrPostUrl: String = "http://$localhost:3000/api/result"
}