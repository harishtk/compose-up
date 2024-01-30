package com.example.compose.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.designsystem.component.ComposeUpBackground
import com.example.compose.ui.theme.ComposeUpTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun CalendarRoute() {

    val dataSource = rememberCalendarDataSource()
    Calendar(dataSource = dataSource)
}

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    dataSource: CalendarDateSource,
) {
    var calendarUiModel by remember {
        mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today))
    }

    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Header(
            modifier = Modifier.fillMaxWidth(),
            uiModel = calendarUiModel,
            onPrevClick = { startDate ->
                val finalStartDate = startDate.minusDays(1)
                calendarUiModel = dataSource.getData(
                    startDate = finalStartDate,
                    lastSelectedDate = calendarUiModel.selectedDate.date
                )
            },
            onNextClick = { endDate ->
                val finalStartDate = endDate.plusDays(2)
                calendarUiModel = dataSource.getData(
                    startDate = finalStartDate,
                    lastSelectedDate = calendarUiModel.selectedDate.date
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Content(
            modifier = Modifier.fillMaxWidth(),
            uiModel = calendarUiModel,
            onDateClick = { date ->
                calendarUiModel = calendarUiModel.copy(
                    selectedDate = date,
                    visibleDates = calendarUiModel.visibleDates.map {
                        it.copy(
                            isSelected = it.date.isEqual(date.date)
                        )
                    }
                )
            }
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiModel: CalendarUiModel,
    onDateClick: (CalendarUiModel.CustomDate) -> Unit = {}
) {
    LazyRow(
        modifier
    ) {
        items(items = uiModel.visibleDates) { date ->
           ContentItem(date = date, onClick = onDateClick)
        }
    }
}

@Composable
private fun ContentItem(
    modifier: Modifier = Modifier,
    date: CalendarUiModel.CustomDate,
    onClick: (CalendarUiModel.CustomDate) -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable { onClick(date) },
        colors = CardDefaults.cardColors(
            containerColor = if (date.isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondary
            }
        )
    ) {
        Column(
            Modifier
                .width(40.dp)
                .height(48.dp)
                .padding(4.dp)
        ) {
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    uiModel: CalendarUiModel,
    onPrevClick: (LocalDate) -> Unit = {},
    onNextClick: (LocalDate) -> Unit = {},
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onPrevClick(uiModel.startDate.date)}) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "Previous",
                modifier = Modifier.size(48.dp)
            )
        }

        val text = if (uiModel.selectedDate.isToday) {
            "Today"
        } else {
            uiModel.selectedDate.date.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier,
        )

        IconButton(onClick = { onNextClick(uiModel.startDate.date) }) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Next",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun CalendarPreview() {
    ComposeUpTheme {
        ComposeUpBackground {
            Box(modifier = Modifier.fillMaxSize()) {
                Calendar(
                    dataSource = rememberCalendarDataSource()
                )
            }
        }
    }
}