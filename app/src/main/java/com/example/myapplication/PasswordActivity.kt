package com.example.myapplication


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


class PasswordActivity : AppCompatActivity() {
    private val atLeastOneUppercase = Regex(".*[A-Z].*")
    private val atLeastOneDigit = Regex(".*\\d.*")
    private val atLeastOneSpecial = Regex(".*\\W.*")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val button : Button = findViewById(R.id.reset)
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "xyz",
            masterKey,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        button.setOnClickListener {
            val oldP: EditText = findViewById(R.id.oldpass)
            val newP: EditText = findViewById(R.id.newpass1)
            val newPR: EditText = findViewById(R.id.newpass2)

            fun validPassword(): Boolean{
                if(oldP.text.isEmpty()){
                    oldP.error = "Old password required."
                    return false
                }
                if(newP.text.toString().isEmpty()){
                    newP.error = "Enter new password"
                    return false
                    }
                if(newPR.text.toString().isEmpty()){
                    newPR.error = "Confirm new password."
                        return false
                    }
                if(newPR.text.toString() != newP.text.toString()){
                    newPR.error = "Password does not match."
                    return false
                }
                if(oldP.text.toString() != sharedPreferences.getString("passwd", "")){
                    oldP.error = "Wrong password."
                    return false
                }
                if(!atLeastOneUppercase.matches(newP.text.toString())){
                    newP.error = "Required at least one uppercase."
                    return false
                }
                if(!atLeastOneDigit.matches(newP.text.toString())){
                    newP.error = "Required at least one digit."
                    return false
                }
                if(!atLeastOneSpecial.matches(newP.text.toString())){
                    newP.error = "Required at least one special."
                    return false
                }
                    return true
            }

            if(validPassword()){
                Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putString("passwd", newP.text.toString()).apply()
                val intentChangePasswordActivity = Intent(applicationContext, MainActivity::class.java)
                startActivity(intentChangePasswordActivity)
                }

        }
    }



}