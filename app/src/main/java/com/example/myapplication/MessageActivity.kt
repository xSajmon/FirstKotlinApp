package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.utils.AppPreferences
import java.nio.charset.Charset
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class MessageActivity : AppCompatActivity() {
    val KEY_ALIAS = "kluczyk2"
    val IV_SEPARATOR = "]"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val editText: EditText = findViewById(R.id.messageBody)
        val sharedPreferences = AppPreferences.getInstance()


//        generateSecretKey(KeyGenParameterSpec.Builder(
//            KEY_ALIAS,
//            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
//            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
//            .build())
        val secretKey = getSecretKey()
        val cipher = getCipher()

        var encodedString: String
        val data = sharedPreferences.getString("msg", "")
        val split = data?.split(IV_SEPARATOR.toRegex())
        val ivString = split!![0]
        encodedString = split[1]
        val ivSpec = IvParameterSpec(Base64.decode(ivString, Base64.DEFAULT))
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)


        val encryptedData = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedData = cipher.doFinal(encryptedData)

        editText.setText(String(decodedData))
//        editText.setText(sharedPreferences.getString("msg", ""))
        val button: Button = findViewById(R.id.saveBtn)


        button.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setNegativeButton("No") { dialog, which ->

                }
                .setPositiveButton("Yes") { dialog, which ->
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                    var result = ""
                    val iv = cipher.iv
                    val ivStr = Base64.encodeToString(iv, Base64.DEFAULT)
                    result = ivStr + IV_SEPARATOR
                    val dataToEncrypt: String = editText.text.toString()
                    val dane :ByteArray = dataToEncrypt.toByteArray()
                    val encryptedDane = cipher.doFinal(dane)
                    result += Base64.encodeToString(encryptedDane, Base64.DEFAULT)

                    sharedPreferences.edit().putString("msg",result).apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            builder.show()
        }


    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")

        keyStore.load(null)
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
    }
    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
    }

}