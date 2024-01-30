package com.example.compose.calendar

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CalendarUiModel(
    val selectedDate: CustomDate,
    val visibleDates: List<CustomDate>
) {
    val startDate: CustomDate = visibleDates.first()
    val endDate: CustomDate = visibleDates.last()

    data class CustomDate(
        val date: LocalDate,
        val isSelected: Boolean,
        val isToday: Boolean
    ) {
        val day: String = date.format(DateTimeFormatter.ofPattern("E"))
    }
}