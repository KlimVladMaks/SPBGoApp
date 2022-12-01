package com.example.spbgo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spbgo.databinding.ActivityEventsListBinding


// Activity со списком мероприятий
class EventsListActivity : AppCompatActivity() {

    // Добавляем биндинг и адаптер
    private lateinit var binding: ActivityEventsListBinding
    private lateinit var adapter: EventsAdapter

    private lateinit var eventsListApi: MutableList<Event>

    // Добавляем геттер для получения доступа к EventsService
    private val eventsService: EventsService
        get() = (applicationContext as EventsApp).eventsService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Убираем верхний заголовок с названиме приложения
        supportActionBar?.hide()

        // Инициализируем биндинг
        binding = ActivityEventsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициалиируем адаптер
        adapter = EventsAdapter()

        // Настройка RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.eventsList.layoutManager = layoutManager
        binding.eventsList.adapter = adapter

        // Активируем слушателя
        eventsService.addListener(eventsListener)

        // Получаем данные с сервера в отдельном потоке
        val api = EventsApi()
        Thread{
            eventsListApi = api.getRequest()
            this@EventsListActivity.runOnUiThread(java.lang.Runnable {
                updateEventsList()
            })
        }.start()
    }

    // Удаляем слушателя для избежания утечек памяти
    override fun onDestroy() {
        super.onDestroy()
        eventsService.removeListener(eventsListener)
    }

    // Добавляем слушателя для отслеживания изменений в RecyclerView
    private val eventsListener: EventsListener = {
        adapter.events = it
    }

    // Обновяем список мероприятий, используя данные с сервера
    fun updateEventsList() {
        adapter.events = eventsListApi
    }
}


