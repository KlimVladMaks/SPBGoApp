package com.example.spbgo

import java.util.*

// Класс для хранения данных об отдельном мероприятии
// Включает: ID, название, иллюстрацию, дату, день недели, платность/бесплатность
data class Event(
    val id: UUID,
    val title: String,
    val image: String,
    val date: String,
    val dayOfWeek: String,
    val isPaid: Boolean,
    val siteUrl: String
)



