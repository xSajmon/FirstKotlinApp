package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.security.KeyStore
import java.security.SecureRandom
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


class MainActivity : AppCompatActivity() {
    val KEY_ALIAS = "kluczyk"

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(applicationContext, MessageActivity::class.java))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verify your identity:")
            .setNegativeButtonText("Cancel")
            .build()
//
//        generateSecretKey(KeyGenParameterSpec.Builder(
//            KEY_ALIAS,
//            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
//            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
//            .setUserAuthenticationRequired(true)
//            .setInvalidatedByBiometricEnrollment(true)
//            .build())


        val image = findViewById<ImageView>(R.id.imageView)
        image.setOnClickListener{
            val cipher = getCipher()
            val secretKey = getSecretKey()
            try{
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
            } catch (e: KeyPermanentlyInvalidatedException){
                finish()
            }



        }

    }

    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")

        keyStore.load(null)
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
    }

    override fun onBackPressed() {}


}
