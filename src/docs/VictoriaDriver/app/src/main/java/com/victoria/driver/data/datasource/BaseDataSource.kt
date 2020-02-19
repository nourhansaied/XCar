package com.victoria.driver.data.datasource

import android.text.TextUtils
import com.victoria.driver.data.pojo.DataWrapper
import com.victoria.driver.data.pojo.ResponseBody
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.File
import java.util.*

open class BaseDataSource {

    fun <T> execute(observable: Single<ResponseBody<T>>): Single<DataWrapper<T>> {
        return observable
                /*.subscribeOn(Schedulers.from(appExecutors.networkIO()))
                .observeOn(Schedulers.from(appExecutors.mainThread()))*/
                .subscribeOn(Schedulers.io())
                .map { t -> DataWrapper(t, null) }
                .onErrorReturn { t -> DataWrapper(null, t) }
                .map {
                    if (it.responseBody != null) {
                        when (it.responseBody.responseCode) {
                            /*0 -> return@map DataWrapper(it.responseBody, ServerException(it.responseBody.message, it.responseBody.responseCode))*/
                        }
                    }
                    return@map it
                }
    }

    protected fun arrayImagePart(keys: HashMap<String, String>): List<MultipartBody.Part> {

        val parts = mutableListOf<MultipartBody.Part>()
        for (entry in keys.entries) {
            val key = entry.key
            val value = entry.value
            val file = File(value)
            val requestBody = MultipartBody.create(MediaType.parse("image/*"), file)
            val formData = MultipartBody.Part.createFormData(key, file.name, requestBody)
            parts.add(formData)
        }
        return parts
    }

    protected fun arrayImagePart(key: String, path: List<String>?): List<MultipartBody.Part> {

        val parts = ArrayList<MultipartBody.Part>()
        if (path != null && !path.isEmpty()) {
            for (i in path.indices) {
                val file = File(path[i])
                val requestBody = MultipartBody.create(MediaType.parse("image/*"), file)
                val formData = MultipartBody.Part.createFormData(key, file.name, requestBody)
                parts.add(formData)
            }
        }
        return parts
    }

    protected fun singleImagePart(key: String, path: String): MultipartBody.Part? {
        var formData: MultipartBody.Part? = null
        if (path != "") {
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                val requestBody = MultipartBody.create(MediaType.parse("image/*"), file)
                formData = MultipartBody.Part.createFormData(key, file.name, requestBody)
            }
            return formData
        }
        return formData
    }

}