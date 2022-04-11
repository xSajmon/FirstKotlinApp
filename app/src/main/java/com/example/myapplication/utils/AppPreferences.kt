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


    }
}