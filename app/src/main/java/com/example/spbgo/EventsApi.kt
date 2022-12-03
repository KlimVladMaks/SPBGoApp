package com.example.spbgo

import android.util.Log
import org.json.JSONArray
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class EventsApi {

    // URL для обращения к серверу
    private val url = "http://77.234.215.138:60866/spbgo/api/events?offset=0&limit=100"

    // Функция для отправки запроса к серверу, возвращающая список мероприятий
    fun getRequest(): MutableList<Event> {

        // Переменные для храненния полученных данных
        var eventsText: String = ""
        val eventsList: MutableList<Event> = mutableListOf()

        // Устанавливаем соединения с сервером
        val httpURLConnection = URL(url).openConnection() as HttpURLConnection

        try {
            // Отправляем GET-запрос
            httpURLConnection.apply {
                connectTimeout = 60000
                requestMethod = "GET"
                doInput = true
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
                val event = Event(
                    id = UUID.randomUUID(),
                    title = title,
                    image = image,
                    date = date,
                    dayOfWeek = weekday,
                    isPaid = !isFree
                )
                eventsList.add(event)
            }

        } catch (e: Exception) {
            // При возникновении ошибки, возвращаем пустой список
            return mutableListOf()

        } finally {
            // Разрываем соединение
            httpURLConnection.disconnect()
        }
        // Возвращаем список мероприятий
        return eventsList
    }
}



