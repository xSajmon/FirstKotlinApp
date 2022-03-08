package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class PasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val queue = Volley.newRequestQueue(this)
        val button : Button = findViewById(R.id.reset)
        val oldPasswd= intent.getStringExtra("passwd")

        button.setOnClickListener {
            val oldp: EditText = findViewById(R.id.oldpass)
            val newp: EditText = findViewById(R.id.newpass1)
            val newpr: EditText = findViewById(R.id.newpass2)

            fun validPassword(): Boolean{
                if(oldp.text.toString().equals("")){
                    oldp.error = "Old password required."
                    return false
                }
                if(newp.text.toString().equals("")){
                    newp.error = "Enter new password"
                    return false
                    }
                if(newpr.text.toString().equals("")){
                    newpr.error = "Confirm new password."
                        return false
                    }
                if(newpr.text.toString() != newp.text.toString()){
                    newpr.error = "Password does not match."
                    return false
                }
                if(oldp.text.toString() != oldPasswd){
                    oldp.error = "Wrong password."
                    return false
                }
                    return true
            }

            if(validPassword()){
                Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show()
                val url = "https://10.0.2.2:443/reset-password/${oldPasswd}/${newpr.text}"
                val stringRequest = StringRequest(
                    Request.Method.POST, url,
                    { response ->
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    }, { error ->
                    })
                queue.add(stringRequest)


                }

        }
    }



}