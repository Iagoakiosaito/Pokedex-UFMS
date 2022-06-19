package dev.progMob.pokeapiandroidtask.utils

import android.content.Context
import android.preference.PreferenceManager
import android.util.Base64
import android.widget.Toast
import dev.progMob.pokeapiandroidtask.keys.iv
import dev.progMob.pokeapiandroidtask.keys.salt
import dev.progMob.pokeapiandroidtask.keys.secretKey
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

//fun encrypt(context: Context, strToEncrypt: String): ByteArray {
//    val plainText = strToEncrypt.toByteArray(Charsets.UTF_8)
//    val keygen = KeyGenerator.getInstance("AES")
//    keygen.init(256)
//    val key = keygen.generateKey()
//    saveSecretKey(context, key)
//    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
//    cipher.init(Cipher.ENCRYPT_MODE, key)
//    val cipherText = cipher.doFinal(plainText)
//    saveInitializationVector(context, cipher.iv)
//
//    val sb = StringBuilder()
//    for (b in cipherText) {
//        sb.append(b.toChar())
//    }
////    Toast.makeText(context, "dbg encrypted = [" + sb.toString() + "]", Toast.LENGTH_LONG).show()
//
//    return cipherText
//}
//
//fun decrypt(context:Context, dataToDecrypt: ByteArray): ByteArray {
//    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
//    val ivSpec = IvParameterSpec(getSavedInitializationVector(context))
//    cipher.init(Cipher.DECRYPT_MODE, getSavedSecretKey(context), ivSpec)
//    val cipherText = cipher.doFinal(dataToDecrypt)
//
//    val sb = StringBuilder()
//    for (b in cipherText) {
//        sb.append(b.toChar())
//    }
//    Toast.makeText(context, "dbg decrypted = [$sb]", Toast.LENGTH_LONG).show()
//
//    return cipherText
//}
//
//fun saveSecretKey(context:Context, secretKey: SecretKey) {
//    val baos = ByteArrayOutputStream()
//    val oos = ObjectOutputStream(baos)
//    oos.writeObject(secretKey)
//    val strToSave = String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT))
//    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
//    val editor = sharedPref.edit()
//    editor.putString("secret_key", strToSave)
//    editor.apply()
//}
//
//fun getSavedSecretKey(context: Context): SecretKey {
//    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
//    val strSecretKey = sharedPref.getString("secret_key", "")
//    val bytes = android.util.Base64.decode(strSecretKey, android.util.Base64.DEFAULT)
//    val ois = ObjectInputStream(ByteArrayInputStream(bytes))
//    return ois.readObject() as SecretKey
//}
//
//fun saveInitializationVector(context: Context, initializationVector: ByteArray) {
//    val baos = ByteArrayOutputStream()
//    val oos = ObjectOutputStream(baos)
//    oos.writeObject(initializationVector)
//    val strToSave = String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT))
//    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
//    val editor = sharedPref.edit()
//    editor.putString("initialization_vector", strToSave)
//    editor.apply()
//}
//
//fun getSavedInitializationVector(context: Context): ByteArray {
//    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
//    val strInitializationVector = sharedPref.getString("initialization_vector", "")
//    val bytes = android.util.Base64.decode(strInitializationVector, android.util.Base64.DEFAULT)
//    val ois = ObjectInputStream(ByteArrayInputStream(bytes))
//    return ois.readObject() as ByteArray
//}

fun encrypt(strToEncrypt: String) :  String?
{
    try
    {
        val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 10000, 256)
        val tmp = factory.generateSecret(spec)
        val secretKey =  SecretKeySpec(tmp.encoded, "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
        return Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)), Base64.DEFAULT)
    }
    catch (e: Exception)
    {
        println("Error while encrypting: $e")
    }
    return null
}

fun decrypt(strToDecrypt : String) : String? {
    try
    {

        val ivParameterSpec =  IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 10000, 256)
        val tmp = factory.generateSecret(spec);
        val secretKey =  SecretKeySpec(tmp.encoded, "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        return  String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)))
    }
    catch (e : Exception) {
        println("Error while decrypting: $e");
    }
    return null
}
