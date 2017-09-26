package id.kibar.api.data.entity

import java.time.LocalDate

data class Activity (
    var id: Int = 0,
    val name: String,
    val date: LocalDate,
    val description: String
)