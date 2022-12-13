package com.example.spbgo.SignInActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spbgo.EventsListActivity
import com.example.spbgo.MainActivity
import com.example.spbgo.R
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignInActivity : AppCompatActivity() {
    private var registrationButton: Button? = null
    private var signInButton: Button? = null
    var isLoginFound = false
    var isPasswordRight = false
    var stringPassword: String? = null
    var stringLogin: String? = null


    @SuppressLint("ResourceAsColor")
    fun btnEnable() {
        if (isLoginFound && isPasswordRight) {
            signInButton?.setBackgroundColor(R.color.colorMain)
            signInButton?.isClickable = true
        } else {
            signInButton?.isClickable = false
            signInButton?.setBackgroundColor(Color.GRAY)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_window)

        val clickLogIn = findViewById<EditText>(R.id.logInField)
        val clickPassword = findViewById<EditText>(R.id.passwordField)
        registrationButton = findViewById(R.id.btnRegistration)
        signInButton = findViewById(R.id.btnSignIn)
        btnEnable()

        /*
        fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE)
        val prefs = customPreference(this, "SPBGo")
        */

        val tokenBeing = getSharedPreferences("SPBGo", MODE_PRIVATE).contains("token")

        if (tokenBeing) {
            val token = getSharedPreferences("SPBGo", MODE_PRIVATE).getString("token", "-1")
            // Toast.makeText(this, token, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, EventsListActivity::class.java))
            finish()
        }

        registrationButton?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


        signInButton?.setOnClickListener {
            if (stringLogin != null && stringPassword != null) {

                getToken(stringLogin!!, stringPassword!!)
                startActivity(Intent(this, EventsListActivity::class.java))
                finish()
            }
        }


        clickLogIn.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s1: Editable?) {
            }

            override fun beforeTextChanged(s1: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s1: CharSequence?, start: Int, before: Int, count: Int) {
                if (s1 != null) {
                    if (s1.length in 3..15) {
                        isLoginFound = true
                        btnEnable()
                        stringLogin = s1.toString()
                        // Toast.makeText(this@MainActivity,"Логин хорош",Toast.LENGTH_SHORT).show()
                        // signInButton.setBackgroundColor(Color.BLUE)
                    } else {
                        isLoginFound = false
                        btnEnable()
                    }
                }
            }
        })

        clickPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s2: Editable?) {
            }

            override fun beforeTextChanged(s2: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s2: CharSequence?, start: Int, before: Int, count: Int) {
                if (s2 != null) {
                    if (s2.length in 5..20) {
                        isPasswordRight = true
                        stringPassword = s2.toString()
                        //Toast.makeText(this@MainActivity,first_password,Toast.LENGTH_SHORT).show()
                        btnEnable()
                        // Toast.makeText(this@MainActivity,"Пароль хорош",Toast.LENGTH_SHORT).show()
                        // signInButton.setBackgroundColor(Color.BLUE)
                    } else {
                        isPasswordRight = false
                        btnEnable()
                    }
                }
            }
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getToken(login: String, password: String) {
        // Toast.makeText(this, "хорош", Toast.LENGTH_SHORT).show()

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("login", login)
        jsonObject.put("password", password)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("http://77.234.215.138:60866/spbgo/api/signin")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty(
                "Content-Type",
                "application/json"
            ) // The format of the content we're sending to the server
            httpURLConnection.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true

            // Send the JSON we created
            val outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
            outputStreamWriter.write(jsonObjectString)
            outputStreamWriter.flush()

            // Check if the connection is successful
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {
                    // Convert raw JSON to pretty JSON using GSON library
                    // val gson = GsonBuilder().setPrettyPrinting().create()
                    print(response)
                    val jsonObject = JSONTokener(response).nextValue() as JSONObject
                    // ID
                    val token = jsonObject.getString("access_token")
                    // Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                    val sharedPreference = getSharedPreferences("SPBGo", MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putString("token", token)
                    editor.apply()
                }
            } else {
                print("Error")
            }
        }
    }
}

