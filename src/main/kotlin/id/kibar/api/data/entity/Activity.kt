package id.kibar.api.data.entity

import java.sql.ResultSet
import java.time.LocalDate

data class Activity (
    var id: Int = 0,
    val name: String,
    val date: LocalDate,
    val description: String
)

enum class ActivityInteraction {
    REGISTER, CHECKIN,
}

class ActivityFactory {
    fun fromRow(row: ResultSet): Activity {
        return Activity(
            id = row.getInt("id"),
            name = row.getString("name"),
            date = row.getDate("activity_date").toLocalDate(),
            description = row.getString("description")
        )
    }
}