package com.example.myapplication


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nuke()

        val button = findViewById<Button>(R.id.showMessageBtn)

        button.setOnClickListener  {

            val queue = Volley.newRequestQueue(this)
            val intent = Intent(this, MessageActivity::class.java)
            val password = findViewById<EditText>(R.id.passwordField)
            val url = "https://10.0.2.2:443/get-message/${password.text}"


            if (password.text.toString() != "") {

                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        intent.putExtra("response", response)
                        intent.putExtra("passwd", password.text.toString())
                        startActivity(intent)
                        Toast.makeText(
                            applicationContext,
                            "Correct.",
                            Toast.LENGTH_LONG
                        ).show()},
                    { error ->
                        password.error = "Wrong password."
                    })
                println(url)
                queue.add(stringRequest)
            } else {
                password.error = "Enter password."
            }
        }
    }


    // disable ssl certificate for testing
    private fun nuke() {
        try {
            val trustAllCerts: Array<TrustManager> = arrayOf(
                @SuppressLint("CustomX509TrustManager")
                object : X509TrustManager {
                    val acceptedIssuers: Array<Any?>
                        get() = arrayOfNulls(0)

                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                    override fun getAcceptedIssuers(): Array<out X509Certificate>? {
                        TODO()
                    }
                }
            )
            val sc: SSLContext = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier(object : HostnameVerifier {
                override fun verify(arg0: String?, arg1: SSLSession?): Boolean {
                    return true
                }
            })
        } catch (e: Exception) {
        }
    }
    override fun onBackPressed() {

    }
}