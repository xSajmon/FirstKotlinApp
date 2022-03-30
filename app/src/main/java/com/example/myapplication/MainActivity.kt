package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.myapplication.utils.AppPreferences
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {
    var clickCount = 0
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

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
                    val intent = Intent(applicationContext, MessageActivity::class.java)
                    startActivity(intent)
                    super.onAuthenticationSucceeded(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verify your identity:")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()


        val sharedPreferences = AppPreferences.getInstance()


        if (!sharedPreferences.contains("passwd")) {
            val dialog = InitialPasswordFragment()
            dialog.show(supportFragmentManager, "initialPasswordDialog")
        }


        val button = findViewById<Button>(R.id.showMessageBtn)

        button.setOnClickListener {

            val password = findViewById<EditText>(R.id.passwordField)

            if (password.text.toString() != "") {
                if (password.text.toString() == sharedPreferences.getString("passwd", "")) {
                    biometricPrompt.authenticate(promptInfo)
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if(id == R.id.delete_button){
            AppPreferences.getInstance().edit().remove("passwd").apply()
            finish()
            startActivity(Intent(applicationContext, MainActivity::class.java))

            overridePendingTransition(1,1)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}