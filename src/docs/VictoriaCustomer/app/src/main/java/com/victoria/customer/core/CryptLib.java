package com.victoria.customer.core;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/*
 * https://github.com/skavinvarnan/Cross-Platform-AES/blob/master/Android/CryptLib.java
 * */

@Singleton
public class CryptLib {

    private final String aesKey;
    private final String ivKey;

    /**
     * Encryption mode enumeration
     */
    private enum EncryptMode {
        ENCRYPT, DECRYPT
    }

    // cipher to be used for encryption and decryption
    private Cipher _cx;

    // encryption key and initialization vector
    private byte[] _key, _iv;

    @Inject
    public CryptLib(@Named("aes-key") String aesKey, @Named("iv-key") String ivKey) {
        this.aesKey = aesKey;
        this.ivKey = ivKey;
        // initialize the cipher with transformation AES/CBC/PKCS5Padding
        try {
            // initialize the cipher with transformation AES/CBC/PKCS5Padding
            _cx = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        _key = new byte[32]; //256 bit key space
        _iv = new byte[16]; //128 bit IV
    }

    /**
     * @param _inputText     Text to be encrypted or decrypted
     * @param _encryptionKey Encryption key to used for encryption / decryption
     * @param _mode          specify the mode encryption / decryption
     * @param _initVector    Initialization vector
     * @return encrypted or decrypted string based on the mode
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private String encryptDecrypt(String _inputText, byte[] _encryptionKey,
                                  EncryptMode _mode, String _initVector) throws UnsupportedEncodingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        String _out = "";

        int len = _encryptionKey.length; // length of the key	provided

        if (_encryptionKey.length > _key.length)
            len = _key.length;

        int ivlen = _initVector.getBytes("UTF-8").length;

        if (_initVector.getBytes("UTF-8").length > _iv.length)
            ivlen = _iv.length;

        System.arraycopy(_encryptionKey, 0, _key, 0, len);
        System.arraycopy(_initVector.getBytes("UTF-8"), 0, _iv, 0, ivlen);
        //KeyGenerator _keyGen = KeyGenerator.getInstance("AES");
        //_keyGen.init(128);

        SecretKeySpec keySpec = new SecretKeySpec(_key, "AES"); // Create a new SecretKeySpec
        // for the
        // specified key
        // data and
        // algorithm
        // name.

        IvParameterSpec ivSpec = new IvParameterSpec(_iv); // Create a new
        // IvParameterSpec
        // instance with the
        // bytes from the
        // specified buffer
        // iv used as
        // initialization
        // vector.

        // encryption
        if (_mode.equals(EncryptMode.ENCRYPT)) {
            _cx.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);// Initialize this cipher instance
            byte[] results = _cx.doFinal(_inputText.getBytes("UTF-8")); // Finish
            _out = Base64.encodeToString(results, Base64.NO_WRAP); // ciphertext
        }

        if (_mode.equals(EncryptMode.DECRYPT)) {
            _cx.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);// Initialize this ipher instance

            byte[] decodedValue = Base64.decode(_inputText.getBytes(), Base64.NO_WRAP);
            byte[] decryptedVal = _cx.doFinal(decodedValue); // Finish
            _out = new String(decryptedVal);
        }
        return _out; // return encrypted/decrypted string
    }

    /***
     * This function computes the SHA256 hash of input string
     * @param text input text whose SHA256 hash has to be computed
     * @param length length of the text to be returned
     * @return returns SHA256 hash of input text
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private byte[] SHA256(String text, int length) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String resultStr;
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();

        StringBuilder result = new StringBuilder();
        for (byte b : digest) {
            result.append(String.format("%02x", b)); //convert to hex
        }
        //return result.toString();

        if (length > result.toString().length()) {
            resultStr = result.toString();
        } else {
            resultStr = result.toString().substring(0, length);
        }

        return resultStr.getBytes("UTF-8");

    }


    /***
     * This function encrypts the plain text to cipher text using the key
     * provided. You'll have to use the same key for decryption
     *
     * @param _plainText
     *            Plain text to be encrypted
     * @return returns encrypted (cipher) text
     */
    public String encryptSimple(String _plainText) {
        try {
            return encryptDecrypt(_plainText, SHA256(aesKey, 32), EncryptMode.ENCRYPT, ivKey);
        } catch (BadPaddingException
                | InvalidKeyException
                | UnsupportedEncodingException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /***
     * This funtion decrypts the encrypted text to plain text using the key
     * provided. You'll have to use the same key which you used during
     * encryprtion
     *
     * @param _encryptedText
     *            Encrypted/Cipher text to be decrypted
     */
    public String decryptSimple(String _encryptedText) {
        try {
            return encryptDecrypt(_encryptedText, SHA256(aesKey, 32), EncryptMode.DECRYPT, ivKey);
        } catch (UnsupportedEncodingException
                | InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException
                | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}