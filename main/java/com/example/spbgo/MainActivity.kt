package com.example.spbgo

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    var isButtonEnabled = false
//    var enablingButton = false

    var isNameok = false
    var isLoginok = false
    var isPasswordok = false
    var isPasswordscoincide = false
    var registrationButton:Button? = null
    var firstPassword:String? = null

    fun f(){
        //Toast.makeText(this@MainActivity,
            //"$isNameok $isLoginok $isPasswordok $isPasswordscoincide",Toast.LENGTH_SHORT).show()
        if (isNameok && isLoginok && isPasswordok && isPasswordscoincide) {
            registrationButton?.setBackgroundColor(Color.BLUE)
            registrationButton?.isClickable = true
        }else{
            registrationButton?.isClickable = false
            registrationButton?.setBackgroundColor(Color.GRAY)
        }
    }
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
        registrationButton?.setOnClickListener{
            
        }

        clickName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length in 2..30) {
                        isNameok = true
                        f()
                        // registration_button.setBackgroundColor(Color.BLUE)
                        // Toast.makeText(this@MainActivity,isButtonEnabled.toString(),Toast.LENGTH_SHORT).show()
                    }else{
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
                        f()
                        // Toast.makeText(this@MainActivity,"Логин хорош",Toast.LENGTH_SHORT).show()
                        // registration_button.setBackgroundColor(Color.BLUE)
                    }else{
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
                        isPasswordok=true
                        firstPassword = s2.toString()
                        //Toast.makeText(this@MainActivity,first_password,Toast.LENGTH_SHORT).show()
                        f()
                        // Toast.makeText(this@MainActivity,"Пароль хорош",Toast.LENGTH_SHORT).show()
                        // registration_button.setBackgroundColor(Color.BLUE)
                    }else{
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
                if (s3 != null) {
                    if (s3.length in 5..20) {
                        //Toast.makeText(this@MainActivity,firstPassword,Toast.LENGTH_SHORT).show()
                        isPasswordscoincide = s3.toString() == firstPassword
                        f()
                        // Toast.makeText(this@MainActivity,"Пароли совпадают",Toast.LENGTH_SHORT).show()
                        // registration_button.setBackgroundColor(Color.BLUE)
                    }else{
                        isPasswordscoincide = false
                        f()
                    }
                }
            }
        })

        Toast.makeText(this@MainActivity, isButtonEnabled.toString(), Toast.LENGTH_SHORT).show()

    }

}
