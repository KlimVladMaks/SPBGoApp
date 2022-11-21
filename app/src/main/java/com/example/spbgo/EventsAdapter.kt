package com.example.spbgo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spbgo.databinding.ItemEventBinding

// Создание адаптера для добавления элементов в RecyclerView
class EventsAdapter: RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    // Создаём список мероприятий
    var events: List<Event> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    // Метод для получения количества элементов в списке
    override fun getItemCount(): Int = events.size

    // Метод для генерации нового элемента списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(inflater, parent, false)
        return EventsViewHolder(binding)
    }

    // Метод для обновления элементов списка в соответсвии с переданными данными
    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val event = events[position]
        with(holder.binding) {

            // Заполнение текстовых полей карточки
            eventTitleTextView.text = event.title
            weekdayTextView.text = event.dayOfWeek
            dateTextView.text = event.date

            // Добавление или скрытие пометки о платности мероприятия
            if (event.isPaid) {
                isPaidImageView.alpha = 1F
            }
            else {
                isPaidImageView.alpha = 0F
            }

            // Добавление изображения на фон карточки
            Glide.with(eventImageView)
                .load(event.image)
                .into(eventImageView)
        }
    }

    // Подключаем холдер
    class EventsViewHolder(
        val binding: ItemEventBinding
    ): RecyclerView.ViewHolder(binding.root)

}