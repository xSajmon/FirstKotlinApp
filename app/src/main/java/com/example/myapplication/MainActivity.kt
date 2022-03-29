package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.utils.AppPreferences


class MainActivity : AppCompatActivity() {
    var clickCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = AppPreferences.getInstance(applicationContext)


        if (!sharedPreferences.contains("passwd")) {
            val dialog = InitialPasswordFragment()
            dialog.show(supportFragmentManager, "initialPasswordDialog")
        }


        val button = findViewById<Button>(R.id.showMessageBtn)
        button.setOnClickListener {


            val intent = Intent(this, MessageActivity::class.java)
            val password = findViewById<EditText>(R.id.passwordField)

            if (password.text.toString() != "") {
                if (password.text.toString() == sharedPreferences.getString("passwd", "")) {
                    startActivity(intent)
                } else {

                    clickCount++
                    password.error = "Wrong password."
                    if (clickCount >= 5) {
                        password.clearFocus()
                        button.isEnabled = false
                        button.setTextColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.white
                            )
                        )
                        button.setBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.grey
                            )
                        )
                        val timer = object : CountDownTimer(30000, 1000) {
                            override fun onTick(millis: Long) {
                                sharedPreferences.edit()
                                    .putString("milli", (millis / 1000).toString()).apply()
                                button.text = sharedPreferences.getString("milli", null)


                            }

                            override fun onFinish() {
                                button.setBackgroundColor(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.purple_500
                                    )
                                )
                                button.setText(R.string.show)
                                button.isEnabled = true
                                clickCount = 0
                            }

                        }
                        timer.start()
                    }
                }
            } else {

                password.error = "Enter password."
            }
        }
    }

    override fun onBackPressed() {}
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }
}