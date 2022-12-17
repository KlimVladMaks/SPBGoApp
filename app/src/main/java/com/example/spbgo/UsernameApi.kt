package com.example.spbgo

import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// Класс для получения имени пользователя с сервера
class UsernameApi() {

    // URL для обращения к серверу
    private val url = "http://77.234.215.138:60866/spbgo/api/profile"

    // Функция для отапрвки запроса к серверу
    fun getResponse(access_token: String): String {

        // Строка для записи ответа от сервера
        val usernameResponseText: String

        // Строка для записи имени пользователя
        val username: String

        // Отлавливаем ошибки
        try {
            // Устанавливаем соединения с сервером
            val httpURLConnection = URL(url).openConnection() as HttpURLConnection

            // Указываем параметры GET-запроса
            httpURLConnection.setRequestProperty("Access-Token", access_token)
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.doInput = true
            httpURLConnection.connectTimeout = 300000

            // Считываем входящие данные в формате строки
            val streamReader = InputStreamReader(httpURLConnection.inputStream)
            streamReader.use {
                usernameResponseText = it.readText()
            }

            // Преобразуем входящие данные в JSON-объект
            val usernameJsonObject = JSONObject(usernameResponseText)
            // Вытаскиваем имя пользователя из JSON-объекта
            username = usernameJsonObject.getString("login")

            // Разрываем соединение
            httpURLConnection.disconnect()

            // Возвращаем имя пользователя
            return username

        } catch (e: Exception) {

            // В случае ошибки возвращаем пустую строку
            return ""
        }
    }
}
