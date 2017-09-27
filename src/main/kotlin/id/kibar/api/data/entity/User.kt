package id.kibar.api.data.entity

import java.sql.ResultSet

data class User(
    var id: Int = 0,
    val email: String,
    val firstName: String = "",
    val lastName: String = "",
    val sub: String = "", // The unique Id provided by google from the Oauth process
    val type: UserType = UserType.PARTICIPANT,
    val company: String = "",
    val position: String = ""
) {
    fun mergeUser(onTop: User): User {
        val newId = if (id != 0 ) id else onTop.id
        val newEmail = if (email != "") email else onTop.email
        val newFirstName = if (firstName != "") firstName else onTop.firstName
        val newLastName = if (lastName != "") lastName else onTop.lastName
        val newSub = if (sub != "") sub else onTop.sub
        val newType = onTop.type
        val newCompany = if (company != "") company else onTop.company
        val newPosition = if (position != "") position else onTop.position

        return User(
            id = newId,
            email = newEmail,
            firstName = newFirstName,
            lastName = newLastName,
            sub = newSub,
            type = newType,
            company = newCompany,
            position = newPosition
        )
    }
}

enum class UserType {
    PARTICIPANT, ADMIN,
}

class UserFactory {
    fun fromRow(row: ResultSet): User {
        return User(
            id = row.getInt("id"),
            email = row.getString("email"),
            firstName = row.getString("first_name"),
            lastName = row.getString("last_name"),
            sub = row.getString("google_sub_id"),
            type = UserType.valueOf(row.getString("user_type")),
            company = row.getString("company"),
            position = row.getString("position")
        )
    }
}