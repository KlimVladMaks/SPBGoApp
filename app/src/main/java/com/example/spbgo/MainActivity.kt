package com.example.spbgo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.spbgo.SignInActivity.SignInActivity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    var isNameok = false
    var isLoginok = false
    var isPasswordok = false
    var isPasswordscoincide = false
    private var registrationButton: Button? = null
    var firstPassword: String? = null
    var name: String? = null
    var login: String? = null

    //data class User(var name: String, var login: String,var password: String)
    @SuppressLint("ResourceAsColor")
    fun f() {
        // Toast.makeText( this@MainActivity, "$isNameok $isLoginok $isPasswordok $isPasswordscoincide", Toast.LENGTH_SHORT ).show()
        if (isNameok && isLoginok && isPasswordok && isPasswordscoincide) {
            registrationButton?.setBackgroundColor(Color.BLUE)
            registrationButton?.isClickable = true
        } else {
            registrationButton?.isClickable = false
            registrationButton?.setBackgroundColor(Color.GRAY)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val clickName = findViewById<EditText>(R.id.name)
        val clickLogin = findViewById<EditText>(R.id.logInField)
        val clickPassword = findViewById<EditText>(R.id.passwordField)
        val clickPasswordCheck = findViewById<EditText>(R.id.passwordCheckField)
        registrationButton = findViewById(R.id.btnRegistration)
        f()

        clickName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length in 2..30) {
                        isNameok = true
                        name = s.toString()
                        // Toast.makeText(this@MainActivity, name, Toast.LENGTH_SHORT).show()
                        f()
                        // registration_button.setBackgroundColor(Color.BLUE)
                        // Toast.makeText(this@MainActivity,isButtonEnabled.toString(),Toast.LENGTH_SHORT).show()
                    } else {
                        //registration_button?.setBackgroundColor(Color.GRAY)
                        isNameok = false
                        f()
                    }
                }
            }
        })

        clickLogin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s1: Editable?) {
            }

            override fun beforeTextChanged(s1: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s1: CharSequence?, start: Int, before: Int, count: Int) {
                if (s1 != null) {
                    if (s1.length in 3..15) {
                        isLoginok = true
                        login = s1.toString()
                        f()
                        // Toast.makeText(this@MainActivity,"Логин хорош",Toast.LENGTH_SHORT).show()
                        // registration_button.setBackgroundColor(Color.BLUE)
                    } else {
                        isLoginok = false
                        f()
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
                        isPasswordok = true
                        firstPassword = s2.toString()
                        //Toast.makeText(this@MainActivity,first_password,Toast.LENGTH_SHORT).show()
                        f()
                        // Toast.makeText(this@MainActivity,"Пароль хорош",Toast.LENGTH_SHORT).show()
                        // registration_button.setBackgroundColor(Color.BLUE)
                    } else {
                        isPasswordok = false
                        f()
                    }
                }
            }
        })

        clickPasswordCheck.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s3: Editable?) {
            }

            override fun beforeTextChanged(s3: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s3: CharSequence?, start: Int, before: Int, count: Int) {
                // Toast.makeText(this@MainActivity,s3.toString(),Toast.LENGTH_SHORT).show()
                if (s3 != null) {
                    if (s3.toString() == firstPassword) {
                        isPasswordscoincide = true
                        f()
                    } else {
                        isPasswordscoincide = false
                        f()
                    }
                }
            }
        })

        registrationButton?.setOnClickListener {

            if (isNameok && isLoginok && isPasswordok && isPasswordscoincide) {
                // Toast.makeText(this, "clicking", Toast.LENGTH_SHORT).show()

                sendRegistrationData(name!!, login!!, firstPassword!!)
                getToken(login!!, firstPassword!!)
                startActivity(Intent(this, SignInActivity::class.java))
                finish()

            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun sendRegistrationData(name: String, login: String, password: String) {
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("login", login)
        jsonObject.put("password", password)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("http://77.234.215.138:60866/spbgo/api/signup")
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
                    // val prettyJson = gson.toJson(JsonParser.parseString(response))
                    // Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()

                }
            } else {
                print("Error")
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun getToken(login: String, password: String) {
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
                    // val prettyJson = gson.toJson(parseString(response))
                    // Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
                    val sharedPreference = getSharedPreferences("SPBGo", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putString("token", response)
                    editor.apply()
                }
            } else {
                print("Error")
            }
        }
    }
}
