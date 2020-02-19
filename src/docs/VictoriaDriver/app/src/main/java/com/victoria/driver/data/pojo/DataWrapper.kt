package com.victoria.driver.data.pojo


data class DataWrapper<T>(val responseBody: ResponseBody<T>?, val throwable: Throwable?, var apiCall: Boolean = false)