package com.victoria.customer.core

import android.util.Log

import okhttp3.*
import okio.Buffer
import java.io.IOException
import javax.inject.Inject

/**
 * Created by hlink21 on 29/11/17.
 */

class AESCryptoInterceptor @Inject
constructor(private val aes: CryptLib) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val plainQueryParameters = request.url().queryParameterNames()
        var httpUrl = request.url()
        // Check Query Parameters and encrypt
        if (plainQueryParameters != null && !plainQueryParameters.isEmpty()) {
            val httpUrlBuilder = httpUrl.newBuilder()
            for (i in plainQueryParameters.indices) {
                val name = httpUrl.queryParameterName(i)
                val value = httpUrl.queryParameterValue(i)
                httpUrlBuilder.setQueryParameter(name, aes.encryptSimple(value))
            }
            httpUrl = httpUrlBuilder.build()
        }

        // Get Header for encryption
        val apiKey = request.headers().get(Session.API_KEY)
        val token = request.headers().get(Session.USER_SESSION)
        val newRequest: Request
        val requestBuilder = request.newBuilder()

        // Check if any body and encrypt
        val requestBody = request.body()
        if (requestBody?.contentType() != null) {
            // bypass multipart parameters for encryption
            val isMultipart = requestBody.contentType()!!.type().equals("multipart", ignoreCase = true)
            val bodyPlainText = if (isMultipart) bodyToString(requestBody) else transformInputStream(bodyToString(requestBody))

            if (!isMultipart) {
                if (bodyPlainText != null) {
                    requestBuilder.post(RequestBody.create(MediaType.parse("text/plain"), bodyPlainText));
                }
            } else {
                requestBuilder.post(requestBody)
            }
        }
        // Build the final request
        newRequest = requestBuilder.url(httpUrl)
                .header(Session.API_KEY, aes.encryptSimple(apiKey))
                .header(Session.USER_SESSION, aes.encryptSimple(token))
                .build()


        // execute the request
        val proceed = chain.proceed(newRequest)
        // get the response body and decrypt it.

        val cipherBody = proceed.body()!!.string()
        Log.e(":: Cipher Text ::", cipherBody)
        val plainBody = aes.decryptSimple(cipherBody)

        // create new Response with plaint text body for further process
        return proceed.newBuilder()
                .body(ResponseBody.create(MediaType.parse("text/json"), plainBody.trim { it <= ' ' }))
                .build()

    }

    private fun bodyToString(request: RequestBody?): String? {

        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return null
            return buffer.readUtf8()
        } catch (e: IOException) {
            return null
        }

    }

    private fun transformInputStream(inputStream: String?): String {
        return aes.encryptSimple(inputStream)
    }
}
