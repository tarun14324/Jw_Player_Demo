package com.example.jwplayerdemo.utils

import java.io.IOException
import android.util.Patterns
import android.webkit.URLUtil
import com.google.android.exoplayer2.util.Util.toByteArray
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.Throws

object Util {

    @Throws(IOException::class)
    fun executePost(
        url: String?,
        data: ByteArray?,
        requestProperties: Map<String?, String?>?
    ): ByteArray {
        var urlConnection: HttpURLConnection? = null
        return try {
            urlConnection = URL(url).openConnection() as HttpURLConnection
            urlConnection.requestMethod = "POST"
            urlConnection.doOutput = data != null
            urlConnection.doInput = true
            if (requestProperties != null) {
                for (requestProperty in requestProperties.entries) {
                    urlConnection.setRequestProperty(
                        requestProperty.key,
                        requestProperty.value
                    )
                }
            }
            // Write the request body, if there is one.
            if (data != null) {
                val out: OutputStream = urlConnection.outputStream
                out.use { ou ->
                    ou.write(data)
                }
            }
            // Read and return the response body.
            val inputStream: InputStream = urlConnection.inputStream
            inputStream.use { stream ->
                toByteArray(stream)
            }
        } finally {
            urlConnection?.disconnect()
        }
    }

    fun isValidURL(url: String?) =
        URLUtil.isValidUrl(url) && url?.let { Patterns.WEB_URL.matcher(it).matches() } == true

}