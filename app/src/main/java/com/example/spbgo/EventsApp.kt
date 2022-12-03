package com.example.spbgo

import android.app.Application

// Делаем класс EventsService сингтоном
// Класс EventsApp указан в AndroidManifest как android:name = ".EventsListActivity"
class EventsApp: Application() {

    val eventsService = EventsService()

}