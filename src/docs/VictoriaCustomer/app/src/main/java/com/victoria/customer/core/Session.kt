package com.victoria.customer.core;

import com.victoria.customer.data.pojo.User

/**
 * Created by hlink21 on 11/7/16.
 */
public interface Session {

    var apiKey: String

    var userSession: String

    var userId: String

    val deviceId: String

    var user: User?

    val language: String

    fun setLanguage(language: String)

    fun clearSession()

    companion object {
        const val API_KEY = "API-KEY"
        const val USER_SESSION = "TOKEN"
        val CONTENT_LANGUAGE = "Accept-Language"
        const val USER_ID = "USER_ID"
        const val DEVICE_TYPE = "A"
    }
}
