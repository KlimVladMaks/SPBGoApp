package com.example.spbgo.SignInActivity;

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.spbgo.R

class SignInActivity : AppCompatActivity() {
    var registrationButton: Button? = null
    var isLoginFound = false
    var isPasswordRight = false
    var stringPassword:String? = null

    fun btnEnable() {
        if (isLoginFound && isPasswordRight) {
            registrationButton?.setBackgroundColor(Color.BLUE)
            registrationButton?.isClickable = true
        }else{
            registrationButton?.isClickable = false
            registrationButton?.setBackgroundColor(Color.GRAY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_window)

        val clickLogIn = findViewById<EditText>(R.id.logInField)
        val clickPassword = findViewById<EditText>(R.id.passwordField)
        registrationButton = findViewById(R.id.btnRegistration)
        btnEnable()
        registrationButton?.setOnClickListener{
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
                        // Toast.makeText(this@MainActivity,"Логин хорош",Toast.LENGTH_SHORT).show()
                        // registration_button.setBackgroundColor(Color.BLUE)
                    }else{
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
                        // registration_button.setBackgroundColor(Color.BLUE)
                    }else{
                        isPasswordRight = false
                        btnEnable()
                    }
                }
            }
        })

    }

}
