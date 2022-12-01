package com.example.spbgo

import android.util.Log
import org.json.JSONArray
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class EventsApi {

    private val url = "http://77.234.215.138:60866/spbgo/api/events?offset=0&limit=100"

    fun getRequest(): MutableList<Event> {

        var eventsText: String = ""
        val eventsList: MutableList<Event> = mutableListOf()
        val httpURLConnection = URL(url).openConnection() as HttpURLConnection

        try {
            httpURLConnection.apply {
                connectTimeout = 60000
                requestMethod = "GET"
                doInput = true
            }

            val streamReader = InputStreamReader(httpURLConnection.inputStream)
            streamReader.use {
                eventsText = it.readText()
            }

            val eventsJson = JSONArray(eventsText)

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
            return mutableListOf()

        } finally {
            httpURLConnection.disconnect()
        }
        return eventsList
    }
}



