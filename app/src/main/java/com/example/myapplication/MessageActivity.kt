package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import java.nio.charset.Charset

class MessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val editText: EditText = findViewById(R.id.messageBody)
        editText.setText(intent.getStringExtra("response"))

        val queue = Volley.newRequestQueue(this)
        val password = intent.getStringExtra("passwd")
        val url = "https://10.0.2.2:443/save-message/${password}"
        val button: Button = findViewById(R.id.saveBtn)
        val buttonP: Button = findViewById(R.id.resetBtn)

        button.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setNegativeButton("No") { dialog, which ->

                }
                .setPositiveButton("Yes") { dialog, which ->
                    val intent = Intent(this, MainActivity::class.java)
                    val requestBody = editText.text.toString()
                    val stringRequest = object :StringRequest(
                       Method.POST, url,
                        { response ->
                            startActivity(intent)

                        }, { error ->
                            println(error.message)
                            Toast.makeText(
                                applicationContext,
                                "Failed.",
                                Toast.LENGTH_LONG
                            ).show()
                        })
                    {
                        override fun getBody(): ByteArray {

                            return requestBody.toByteArray(Charset.defaultCharset())
                        }
                    }
                    println(url)
                    println(requestBody)
                    println(stringRequest.body)
                    queue.add(stringRequest)
                }
           builder.show()
        }

        buttonP.setOnClickListener{
            val intentPasswordActivity = Intent(this, PasswordActivity::class.java)
            intentPasswordActivity.putExtra("passwd", password)
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