package com.example.spbgo

import com.example.spbgo.Event
import java.util.*

// Слушатель
typealias EventsListener = (events: List<Event>) -> Unit

// Класс для управления данными для списка мероприятий
class EventsService {

    // Список пользователей
    private var events = mutableListOf<Event>()

    // Список слушателей
    private val listeners = mutableListOf<EventsListener>()

    // Инициализируем список мероприятий
    init {
        events = listOf<Event>(
            Event(1, "Мероприятие №1", "https://images.unsplash.com/photo-1544531586-fde5298cdd40?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80", "11.12.2022", "Понедельник", false),
            Event(2, "Мероприятие №2", "https://images.unsplash.com/photo-1611094607507-8c8173e5cf33?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80", "12.12.2022", "Вторник", true),
            Event(3, "Мероприятие №3", "https://images.unsplash.com/photo-1540575467063-178a50c2df87?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80", "13.12.2022", "Среда", false),
            Event(4, "Мероприятие №4", "https://images.unsplash.com/photo-1518611012118-696072aa579a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80", "14.12.2022", "Четверг", true),
            Event(5, "Мероприятие №5", "https://images.unsplash.com/photo-1513159446162-54eb8bdaa79b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80", "15.12.2022", "Пятница", false),
            Event(6, "Мероприятие №6", "https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80", "16.12.2022", "Суббота", true),
            Event(7, "Мероприятие №7", "None", "17.12.2022", "Воскресенье", false),
            Event(8, "Мероприятие №8", "None", "18.12.2022", "Понедельник", true),
            Event(9, "Мероприятие №9", "None", "19.12.2022", "Вторник", false),
            Event(10, "Мероприятие №10", "None", "20.12.2022", "Среда", true)
        ).toMutableList()
    }

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


