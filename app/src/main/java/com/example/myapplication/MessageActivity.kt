package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class MessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val editText: EditText = findViewById(R.id.messageBody)


        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "xyz",
            masterKey,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        editText.setText(sharedPreferences.getString("msg", ""))
        val button: Button = findViewById(R.id.saveBtn)
        val buttonP: Button = findViewById(R.id.resetBtn)


        button.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setNegativeButton("No") { dialog, which ->

                }
                .setPositiveButton("Yes") { dialog, which ->
                    sharedPreferences.edit().putString("msg", editText.text.toString()).apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
           builder.show()
        }

        buttonP.setOnClickListener{
            val intentPasswordActivity = Intent(this, PasswordActivity::class.java)
            startActivity(intentPasswordActivity)
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {

    }

}