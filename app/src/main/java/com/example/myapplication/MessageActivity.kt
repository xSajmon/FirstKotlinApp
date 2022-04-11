package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.utils.AppPreferences

class MessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val editText: EditText = findViewById(R.id.messageBody)


        val sharedPreferences = AppPreferences.getInstance()

        editText.setText(sharedPreferences.getString("msg", ""))
        val button: Button = findViewById(R.id.saveBtn)


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


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {

    }

}