package com.victoria.driver.core


import com.victoria.driver.data.pojo.User


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
        const val USER_ID = "USER_ID"
        val CONTENT_LANGUAGE = "Accept-Language"

        const val DEVICE_TYPE = "A"
    }
}
