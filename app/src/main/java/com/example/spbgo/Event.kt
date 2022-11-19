package com.example.spbgo

import java.time.DayOfWeek
import java.util.Date

// Класс для хранения данных об отдельном мероприятии
// Включает: ID, название, иллюстрацию, дату, день недели, платность/бесплатность
data class Event(
    val id: Long,
    val title: String,
    val image: String,
    val date: String,
    val dayOfWeek: String,
    val isPaid: Boolean
)



