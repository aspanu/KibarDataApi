package id.kibar.api.data.persistence

import id.kibar.api.data.entity.Activity
import id.kibar.api.data.entity.User
import id.kibar.api.data.entity.UserFactory
import java.sql.Statement
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime


class UserPersistence {

    private val userTable = "users"

    fun addUser(user: User): User {
        val conn = DbPool.getConnection()
        conn.autoCommit = false
        try {
            val stm = conn.prepareStatement(
            "INSERT INTO $userTable (" +
                "first_name, last_name, email, google_sub_id, " +
                "user_type, last_update_time, company, position) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS
            )
            stm.setString(1, user.firstName)
            stm.setString(2, user.lastName)
            stm.setString(3, user.email)
            stm.setString(4, user.sub)
            stm.setString(5, user.type.name)
            stm.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()))
            stm.setString(7, user.company)
            stm.setString(8, user.position)

            if (stm.executeUpdate() != 1) {
                conn.rollback()
                throw IllegalStateException("Insert statement should only update 1 record.")
            }

            val rs = stm.generatedKeys
            rs.first()
            user.id = rs.getInt(1)
        } catch (e: Exception) {
            conn.rollback()
            throw e
        }

        conn.commit()
        return user
    }

    fun updateUser(user: User): User {
        val newUser: User
        if (user.id == 0) {
            val currentUser = getUserWithEmail(user.email)
            newUser = user.mergeUser(currentUser)
        } else
            newUser = user

        val conn = DbPool.getConnection()
        conn.autoCommit = false
        try {
            val stm = conn.prepareStatement(
            "UPDATE $userTable SET " +
                "first_name = ?, last_name = ?, email = ?, google_sub_id = ?, " +
                "user_type = ?, last_update_time = ?, company = ?, position = ?" +
                "WHERE id = ?;",
                Statement.RETURN_GENERATED_KEYS
            )
            stm.setString(1, newUser.firstName)
            stm.setString(2, newUser.lastName)
            stm.setString(3, newUser.email)
            stm.setString(4, newUser.sub)
            stm.setString(5, newUser.type.name)
            stm.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()))
            stm.setString(7, newUser.company)
            stm.setString(8, newUser.position)
            stm.setInt(9, newUser.id)

            if (stm.executeUpdate() != 1) {
                conn.rollback()
                throw IllegalStateException("Update statement should only update 1 record.")
            }
        } catch (e: Exception) {
            conn.rollback()
            throw e
        }

        conn.commit()
        return newUser
    }


    fun hasUserWithSub(sub: String): Boolean {
        val stm = DbPool.getConnection().prepareStatement("SELECT COUNT(*) FROM $userTable WHERE google_sub_id=?")
        stm.setString(1, sub)
        val result = stm.executeQuery()
        result.last()
        if (result.row != 1)
            return false
        result.first()
        return result.getInt(1) == 1
    }

    fun checkIfUserEmailExists(email: String) : Boolean {
        val stm = DbPool.getConnection().prepareStatement("SELECT COUNT(*) FROM $userTable WHERE email=?")
        stm.setString(1, email)
        val result = stm.executeQuery()
        result.last()
        if (result.row != 1)
            return false
        result.first()
        return result.getInt(1) == 1
    }

    fun getUser(userId: Int): User {
        val stm = DbPool.getConnection().prepareStatement("SELECT * FROM $userTable WHERE id=?")
        stm.setInt(1, userId)
        val result = stm.executeQuery()
        result.last()
        if (result.row != 1)
            throw IllegalStateException("User cannot be found or we have multiple users with the same Id")
        result.first()
        return UserFactory().fromRow(result)
    }

    fun getUserWithEmail(email: String): User {
        val stm = DbPool.getConnection().prepareStatement("SELECT * FROM $userTable WHERE email=?")
        stm.setString(1, email)
        val result = stm.executeQuery()
        result.last()
        if (result.row != 1)
            throw IllegalStateException("User cannot be found or we have multiple users with the same email")
        result.first()
        return UserFactory().fromRow(result)
    }
}

class UserActivityPersistence {

    fun checkInUser(user: User, activity: Activity): String {
        val payload = "User with id: ${user.id} checked in for activity: ${activity.name}"
        return payload
    }

    fun registerUser(user: User, activity: Activity): String {
        val payload = "User with id: ${user.id} registered for activity ${activity.name}"
        return payload
    }

}

class ActivityPersistence {
    private val activities = hashMapOf(
        0 to Activity(name = "PWA", date = LocalDate.of(2017, 9, 12), description = "Progressive Web App"),
        1 to Activity(id = 1, name = "Hackathon", date = LocalDate.of(2017, 9, 5), description = "Hackathon with a theme of sports and well-being"),
        2 to Activity(id = 2, name = "Career Fair", date = LocalDate.of(2017, 9, 6), description = "Post hackathon career fair")
    )

    fun getActivity(activityId: Int): Activity {
        if (!activities.containsKey(activityId))
            throw IllegalArgumentException()
        return activities[activityId]!!
    }
}