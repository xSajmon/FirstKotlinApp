package com.example.myapplication.utils


import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.KeyStore
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


class AppPreferences {

    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        private val advancedSpec = KeyGenParameterSpec.Builder(
            "_androidx_security_master_key_",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
//            .setUserAuthenticationRequired(true)
//            .setUserAuthenticationParameters(0, KeyProperties.AUTH_DEVICE_CREDENTIAL)
            .build()

        private val masterKey = MasterKey.Builder(MainApplication.applicationContext())
            .setKeyGenParameterSpec(advancedSpec)
            .build()



        fun getInstance(): SharedPreferences {
            sharedPreferences = EncryptedSharedPreferences.create(
                MainApplication.applicationContext(),
                "xyz",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            return sharedPreferences
        }
        fun put(key: String, value: String){
            sharedPreferences.edit().putString(key, hashing(value)).apply()
        }

        fun check(possiblePassword : String) :Boolean{

            return hashing(possiblePassword) == sharedPreferences.getString("passwd", "")
        }

        fun generateSalt() :ByteArray{
//            val random = SecureRandom()
            val salt = ByteArray(1)
//            random.nextBytes(salt)
            salt[0] = 1

            return salt
        }

        fun hashing(password: String) :String{
            val pass: CharArray = password.toCharArray()
            val salt :ByteArray = generateSalt()
            val spec = PBEKeySpec(pass, salt, 100, 512) // should be at least 10k
            val secretKeyFactory  = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val hash :ByteArray = secretKeyFactory.generateSecret(spec).encoded

            println(password)
            println(String(hash))
            return String(hash)
        }

//        fun getSecretKey(): SecretKey {
//
//            val keyGenerator = KeyGenerator.getInstance(
//                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
//            keyGenerator.init(advancedSpec)
//            keyGenerator.generateKey()
//            val keyStore = KeyStore.getInstance("AndroidKeyStore")
//
//
//            keyStore.load(null)
//            return keyStore.getKey("_androidx_security_master_key_", null) as SecretKey
//        }
//
//        fun getCipher(): Cipher {
//            return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
//                    + KeyProperties.BLOCK_MODE_GCM + "/"
//                    + KeyProperties.ENCRYPTION_PADDING_NONE)
//        }

    }
}