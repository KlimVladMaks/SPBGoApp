package com.example.spbgo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // После запуска приложения сразу переходим в EventsListActivity
        // В итоговой реализации переход должен быть после Activity входа
        val intent = Intent(this@MainActivity, EventsListActivity::class.java)
        startActivity(intent)
    }
}

