package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "xyz",
            masterKey,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )




        val button = findViewById<Button>(R.id.showMessageBtn)

        button.setOnClickListener  {


            val intent = Intent(this, MessageActivity::class.java)
            val password = findViewById<EditText>(R.id.passwordField)

            if (password.text.toString() != "") {
                if(password.text.toString().equals(sharedPreferences.getString("passwd", ""))){
                    startActivity(intent)
                } else {
                    password.error = "Wrong password."
                }


            } else {
                password.error = "Enter password."
            }
        }
    }

    override fun onBackPressed() {

    }
}