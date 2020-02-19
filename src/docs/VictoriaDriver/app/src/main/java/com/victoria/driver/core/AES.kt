package com.victoria.driver.core


import android.util.Base64
import okio.ByteString
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by hlink21 on 28/4/17.
 */
@Singleton
class AES @Inject
constructor(@Named("aes-key") key: String) {

    private var arrKey: ByteArray? = null
    private val ivKey: ByteArray


    init {

        try {
            // arrKey = Base64.decode(key, Base64.DEFAULT);
            arrKey = key.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            arrKey = key.toByteArray()
            e.printStackTrace()
        }

        ivKey = ByteArray(16)
    }

    fun encrypt(plainText: String?): String? {
        try {
            if (plainText != null) {
                val plainTextBytes = plainText.toByteArray(charset("UTF-8"))
                return String(encrypt(plainTextBytes)!!)
            }

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return null
    }


    fun encrypt(plainTextBytes: ByteArray): ByteArray? {

        try {

            System.arraycopy(arrKey!!, 0, ivKey, 0, 16)
            val cipher = Cipher.getInstance("AES/CBC/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(arrKey, "AES"), IvParameterSpec(ivKey))

            val l = plainTextBytes.size
            val r = l % 16
            val n = l - r + 16
            val enc = cipher.doFinal(Arrays.copyOf(plainTextBytes, n))
            return ByteString.of(*enc).base64().toByteArray()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

        return null
    }


    fun decrypt(cypherText: String): String? {

        try {
            return String(decrypt(cypherText.toByteArray(charset("UTF-8")))!!)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return null
    }

    fun decrypt(cypherText: ByteArray?): ByteArray? {

        try {
            if (cypherText != null) {
                System.arraycopy(arrKey!!, 0, ivKey, 0, 16)
                val cipher = Cipher.getInstance("AES/CBC/NoPadding")
                cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(arrKey, "AES"), IvParameterSpec(ivKey))
                val bytes = Base64.decode(cypherText, Base64.DEFAULT)
                if (bytes != null) {
                    val enc = cipher.doFinal(bytes)
                    return enc
                }
            }

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

        return null
    }


}
