package com.example.spbgo

import java.util.*

// Слушатель
typealias EventsListener = (events: MutableList<Event>) -> Unit

// Класс для управления данными для списка мероприятий
class EventsService {

    // Список пользователей
    private var events = mutableListOf<Event>()

    // Список слушателей
    private val listeners = mutableListOf<EventsListener>()

    // Метод для получения списка мероприятий
    fun getEvents(): List<Event> {
        return events
    }

    // Метод для удаления мероприятия
    fun deleteEvent(event: Event) {
        val indexToDelete = events.indexOfFirst { it.id == event.id }
        if (indexToDelete != 1) {
            events.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    // Метод для добавления мероприятия в список
    fun addEvent(event: Event) {
        events.add(event)
    }

    // Метод для перемещаения мероприятия по списку
    fun moveEvent(event: Event, moveBy: Int) {
        val oldIndex = events.indexOfFirst { it.id == event.id }
        if (oldIndex == 1) return
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= events.size) return
        Collections.swap(events, oldIndex, newIndex)
        notifyChanges()
        }

    // Добавить слушателя
    fun addListener(listener: EventsListener) {
        listeners.add(listener)
        listener.invoke(events)
    }

    // Удалить слушателя
    fun removeListener(listener: EventsListener) {
        listeners.remove(listener)
    }

    // Метод для уведомления слушателей об изменении элементов списка
    private fun notifyChanges() {
        listeners.forEach { it.invoke(events) }
    }
}


