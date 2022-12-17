package com.example.spbgo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    // Имя пользователя
    private var username = ""

    // Текстовое поле для имени пользователя
    private lateinit var usernameTextView: TextView

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

        // Узнаём у сервера имя пользователя и вставляем в соответствующее текстовое поле
        val usernameApi = UsernameApi()
        Thread{
            // Даелаем запрос имени до пяти раз
            for (i in 0 until 5) {
                username = usernameApi.getResponse(accessToken)
                if (username != "") break
            }

            // Устанавливаем имя пользователя
            this@EventsListActivity.runOnUiThread(java.lang.Runnable {
                usernameTextView = findViewById(R.id.username)
                usernameTextView.text = username
            })
        }.start()

        // Получаем данные с сервера в отдельном потоке и обновляем список мероприятий
        val api = EventsApi()
        Thread{
            // Можем сделать запрос до пяти раз
            for (i in 0 until 5) {
                // Делаем запрос
                eventsListApi = api.getRequest(accessToken)
                // Если мероприятия получены, то выходим из цикла
                if (eventsListApi.size != 0) break
            }

            // Обновляем список мероприятий в основном потоке
            this@EventsListActivity.runOnUiThread(java.lang.Runnable {

                // Если мероприятий всё равно нет, то выводим сообщение об ошибке
                if (eventsListApi.size == 0) {
                    Toast.makeText(this, "Ошибка подключения", Toast.LENGTH_SHORT).show()
                }

                // Убираем анимацию загрузки и обновляем список
                findViewById<ContentLoadingProgressBar>(R.id.loadingPanel).isVisible = false
                updateEventsList()
            })
        }.start()

        // Инициализируем кнопку выхода
        exitButton = findViewById(R.id.exit_button)

        // Устанавливаем слушателя для реагирования на нажатие на кнопку выхода
        exitButton.setOnClickListener {
            // Выходим из списка мероприятий
            exit()
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
    private fun updateEventsList() {

        // Если возвращён индикатор плохого запроса (bedRequestEvent), то переходим на страницу регистрации
        // (скорее всего, это вызвано истечением срока действия токена)
        if ((eventsListApi.size != 0) && (eventsListApi[0].title == "Bad Request")) {
            exit()
            return
        }

        adapter.events = eventsListApi
    }

    // Функция для выхода (разлогина) с экрана списка мероприятий
    private fun exit() {
        // Удаляем значение токена из памяти
        val editor = getSharedPreferences("SPBGo", Context.MODE_PRIVATE).edit()
        editor.remove("token")
        editor.apply()

        // Переходим на страницу авторизации
        val intent = Intent(this@EventsListActivity, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}
