package com.example.spbgo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spbgo.SignInActivity.SignInActivity
import com.example.spbgo.databinding.ActivityEventsListBinding

// Activity со списком мероприятий
class EventsListActivity : AppCompatActivity() {

    // Добавляем биндинг и адаптер
    private lateinit var binding: ActivityEventsListBinding
    private lateinit var adapter: EventsAdapter

    // Список мероприятий
    private lateinit var eventsListApi: MutableList<Event>

    // Кнопка выхода
    private lateinit var exitButton: ImageView

    // Токен для отправки запроса на сервер
    private lateinit var accessToken: String

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

        // Инициалиируем адаптер и передаём ему слушателя на случай нажатия на элемент списка
        adapter = EventsAdapter(object: EventActionListener {
            override fun openWebPage(event: Event) {
                // При нажатии на карточку мероприятия, переходим на сайт с информацией о мероприятии
                val webpage: Uri = Uri.parse(event.siteUrl)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }
        })

        // Настройка RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.eventsList.layoutManager = layoutManager
        binding.eventsList.adapter = adapter

        // Активируем слушателя
        eventsService.addListener(eventsListener)

        // Создаём экземпляр sharedPreferences и берём токен из памяти
        val sharedPreferences = getSharedPreferences("SPBGo", Context.MODE_PRIVATE)
        accessToken = sharedPreferences.getString("token", "").toString()

        // Получаем данные с сервера в отдельном потоке и обновляем список мероприятий
        val api = EventsApi()
        Thread{
            eventsListApi = api.getRequest(accessToken)
            this@EventsListActivity.runOnUiThread(java.lang.Runnable {
                findViewById<ContentLoadingProgressBar>(R.id.loadingPanel).isVisible = false // Убираем анимацию загрузки
                updateEventsList()
            })
        }.start()

        // Инициализируем кнопку выхода
        exitButton = findViewById(R.id.exit_button)

        // Устанавливаем слушателя для реагирования на нажатие на кнопку выхода
        exitButton.setOnClickListener {
            // Удаляем значение токена из памяти
            val editor = sharedPreferences.edit()
            editor.remove("token")
            editor.apply()

            // Переходим на страницу авторизации
            val intent = Intent(this@EventsListActivity, SignInActivity::class.java)
            startActivity(intent)
        }
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
