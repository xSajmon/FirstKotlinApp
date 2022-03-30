package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.utils.AppPreferences
import com.example.myapplication.utils.Validator


class PasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val button: Button = findViewById(R.id.reset)
        val sharedPreferences = AppPreferences.getInstance()

        button.setOnClickListener {
            val oldP: EditText = findViewById(R.id.oldpass)
            val newP: EditText = findViewById(R.id.newpass1)
            val newPR: EditText = findViewById(R.id.newpass2)

            fun validPassword(): Boolean {

                if (Validator.isEmpty(oldP, "Old password required.")) return false
                if (Validator.isEmpty(newP, "Enter new password.")) return false
                if (Validator.isEmpty(newPR, "Confirm new password.")) return false
                if (!Validator.isMatching(newP, newPR)) return false
                if (!Validator.isPasswordValid(newP)) return false

                if (oldP.text.toString() != sharedPreferences.getString("passwd", "")) {
                    oldP.error = "Wrong password."
                    return false
                }

                return true
            }

            if (validPassword()) {
                Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putString("passwd", newP.text.toString()).apply()
                val intentChangePasswordActivity =
                    Intent(applicationContext, MainActivity::class.java)
                startActivity(intentChangePasswordActivity)
            }

        }
    }


}