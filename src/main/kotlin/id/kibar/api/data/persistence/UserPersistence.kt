package id.kibar.api.data.persistence

import id.kibar.api.data.entity.Activity
import id.kibar.api.data.entity.ActivityFactory
import id.kibar.api.data.entity.ActivityInteraction
import id.kibar.api.data.entity.User
import id.kibar.api.data.entity.UserFactory
import java.sql.Statement
import java.sql.Timestamp
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

    private val userActivityTable = "user_activity"

    private fun createInteraction(userId: Int, activityId: Int, interaction: ActivityInteraction) {
        val conn = DbPool.getConnection()
        conn.autoCommit = false
        try {

            val stm = conn.prepareStatement(
                "INSERT INTO $userActivityTable (user_id, activity_id, interaction_type) VALUES (?, ?, ?);"
            )
            stm.setInt(1, userId)
            stm.setInt(2, activityId)
            stm.setString(3, interaction.name)

            if (stm.executeUpdate() != 1) {
                conn.rollback()
                throw IllegalStateException("Insert statement should only update 1 record.")
            }

        } catch (e: Exception) {
            conn.rollback()
            throw e
        }
        conn.commit()
    }

    fun checkInUser(user: User, activity: Activity) {
        createInteraction(userId = user.id, activityId = activity.id, interaction = ActivityInteraction.CHECKIN)
    }

    fun registerUser(user: User, activity: Activity) {
        createInteraction(userId = user.id, activityId = activity.id, interaction = ActivityInteraction.REGISTER)
    }

}

class ActivityPersistence {

    private val activityTable = "activities"

    fun getActivity(activityId: Int): Activity {
        val stm = DbPool.getConnection().prepareStatement("SELECT * FROM $activityTable WHERE id=?")
        stm.setInt(1, activityId)
        val result = stm.executeQuery()
        result.last()
        if (result.row != 1)
            throw IllegalStateException("Activity cannot be found or we have multiple activities with the same Id")
        result.first()
        return ActivityFactory().fromRow(result)
    }
}