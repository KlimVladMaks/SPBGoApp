package com.example.spbgo

import android.util.Log
import org.json.JSONArray
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

// "url" = "http://77.234.215.138:60866/spbgo/api/events?offset=0&limit=10"
// "access_token" = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyIn0.yX22luuOGrofi4E0oIu5MzhEwmzjtRFs8d4CVJN0Sco"

class EventsApi() {

    // URL для обращения к серверу
    private val url = "http://77.234.215.138:60866/spbgo/api/events?offset=0&limit=60"

    // Функция для отправки запроса к серверу, возвращающая список мероприятий
    fun getRequest(access_token: String): MutableList<Event> {

        // Переменные для храненния полученных данных
        var eventsText: String = ""
        val eventsList: MutableList<Event> = mutableListOf()

        // Отлавливаем ошибки
        try {
            // Устанавливаем соединения с сервером
            val httpURLConnection = URL(url).openConnection() as HttpURLConnection

            // Указываем параметры GET-запроса
            httpURLConnection.setRequestProperty("Access-Token", access_token)
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.doInput = true
            httpURLConnection.connectTimeout = 300000

            // Если ответ сервера равен 400 (Bad Request), то возвращаем список с bedRequestEvent
            if (httpURLConnection.responseCode == 400) {
                val bedRequestEvent = Event(
                    id = UUID.randomUUID(),
                    title = "Bad Request",
                    image = "Bad Request",
                    date = "Bad Request",
                    dayOfWeek = "Bad Request",
                    isPaid = false,
                    siteUrl = "Bad Request"
                )
                return mutableListOf(bedRequestEvent)
            }

            // Считываем входящие данные в формате строки
            val streamReader = InputStreamReader(httpURLConnection.inputStream)
            streamReader.use {
                eventsText = it.readText()
            }

            // Преобразуем входящие данные в JSON-массив
            val eventsJson = JSONArray(eventsText)

            // Перебираем данные из JSON-массива
            // и формируем список с экземплярами класса мероприятий
            for (i in 0 until eventsJson.length()) {
                val eventObject = eventsJson.getJSONObject(i)
                val eventData = eventObject.getJSONObject("event")
                val image = eventData.getString("image")
                val title = eventData.getString("title")
                val description = eventData.getString("description")
                val place = eventData.getString("place")
                val date = eventData.getString("date")
                val isFree = eventData.getBoolean("is_free")
                val weekday = eventData.getString("weekday")
                val siteUrl = eventData.getString("site_url")
                val event = Event(
                    id = UUID.randomUUID(),
                    title = capitalize(title),
                    image = image,
                    date = formatDate(date),
                    dayOfWeek = weekday,
                    isPaid = !isFree,
                    siteUrl = siteUrl
                )
                eventsList.add(event)
            }

            // Разрываем соединение
            httpURLConnection.disconnect()

            // Возвращаем список мероприятий
            return eventsList

        } catch (e: Exception) {
            // При возникновении ошибки, возвращаем пустой список
            return mutableListOf<Event>()
        }
    }

    // Функция для преобразования даты в формат дд.мм.гггг
    private fun formatDate(date: String): String {
        // Выбираем из переданной строки год, месяц и день
        val year = date.slice(0..3)
        val month = date.slice(5..6)
        val day = date.slice(8..9)

        // Возвращаем дату в отформатированном формате
        return "${day}.${month}.${year}"
    }

    // Функция для добавления первой заглавной буквы в строку
    private fun capitalize(string: String): String {
        // Берём первую букву переданной строки, приводим к верхнему регистру
        // и помещаем в начало исходной строки
        var first_letter = string[0].toChar()
        first_letter = first_letter.uppercaseChar()
        return "${first_letter}${string.drop(1)}"
    }
}
