package com.example.myapplication.utils


import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.myapplication.MainActivity

class AppPreferences {

    companion object{
            private lateinit var sharedPreferences: SharedPreferences
            private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            fun getInstance(context: Context) :SharedPreferences {
                sharedPreferences = EncryptedSharedPreferences.create(
                    "xyz",
                    masterKey,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
                return sharedPreferences
        }

    }
}