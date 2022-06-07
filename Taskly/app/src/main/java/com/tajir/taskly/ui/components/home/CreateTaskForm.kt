package com.tajir.taskly.ui.components.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.time.format.DateTimeFormatter.*


@RequiresApi(Build.VERSION_CODES.O)
fun selectDateTime(
    context: Context,
    dt: LocalDateTime,
    changedCallBack: (due_date: LocalDateTime) -> Unit
) {

    val startYear = dt.year
    val startMonth = dt.monthValue
    val startDay = dt.dayOfMonth
    val startHour = dt.hour
    val startMinute = dt.minute

    DatePickerDialog(context, { _, year, month, day ->
        TimePickerDialog(context, { _, hour, minute ->
            val pickedDateTime = Calendar.getInstance()
            pickedDateTime.set(year, month, day, hour, minute)

            val dt = LocalDateTime.ofInstant(
                pickedDateTime.toInstant(),
                pickedDateTime.timeZone.toZoneId()
            )
            changedCallBack(dt)
        }, startHour, startMinute, false).show()
    }, startYear, startMonth, startDay).show()
}

@RequiresApi(Build.VERSION_CODES.O)
fun getStringFromDate(dt: LocalDateTime): String {
    val dtNow = LocalDateTime.now()
    val today = dtNow.toLocalDate()
    val tomorrow = dtNow.toLocalDate().plus(1, ChronoUnit.DAYS)
    val nextWeek = today.plus(1, ChronoUnit.WEEKS)
    val yesterday = today.minus(1, ChronoUnit.DAYS)
    val pastWeek = today.minus(1, ChronoUnit.WEEKS)

    val givenDate = dt.toLocalDate()

    val time = dt.format(ofPattern("hh.mm a"))
    val date: String = if (givenDate == today) {
        "Today"
    } else if (yesterday == givenDate) {
        "Yesterday"
    } else if (tomorrow == givenDate) {
        "Tomorrow"
    } else if (yesterday>givenDate && givenDate > pastWeek) {
        val dayName = dt.format(ofPattern("EEE"))
        String.format("Past %s", dayName)
    } else if (today <= givenDate && givenDate < nextWeek) {
        dt.format(ofPattern("EEE"))
    } else {
        dt.format(ofPattern("MMM dd"))
    }
    return String.format("%s at %s", date, time)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTaskForm(
    title: String?,
    titleChanged: (title: String) -> Unit,
    description: String?,
    descriptionChanged: (desc: String) -> Unit,
    dueDate: LocalDateTime = LocalDateTime.now().with(LocalTime.MAX),
    dueDateChanged: (due_date: LocalDateTime) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current

    Box(modifier = modifier) {
        Column(
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Title")
            OutlinedInputField(
                value = title ?: "",
                modifier = modifier,
                label = "Do ...",
                onChangedHandler = titleChanged
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Description")
            OutlinedInputField(
                value = description ?: "",
                modifier = modifier,
                label = "I have to ...",
                onChangedHandler = descriptionChanged
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    selectDateTime(context, dueDate, dueDateChanged)
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF55B5F1))
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Picker",
                    Modifier.padding(end = 8.dp)
                )
                Text(text = getStringFromDate(dueDate))
            }
        }
    }
}