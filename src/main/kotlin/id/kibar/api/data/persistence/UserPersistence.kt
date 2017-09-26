package id.kibar.api.data.persistence

import id.kibar.api.data.entity.Activity
import id.kibar.api.data.entity.User
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger

class UserPersistence {

    private val users = hashMapOf(
        0 to User(firstName = "Alice", email = "alice@alice.kt", id = 0, lastName = "Test"),
        1 to User(firstName = "Bob", email = "bob@bob.kt", id = 1, lastName = "Test"),
        2 to User(firstName = "Carol", email = "carol@carol.kt", id = 2, lastName = "Test"),
        3 to User(firstName = "Dave", email = "dave@dave.kt", id = 3, lastName = "Test")
    )

    var lastId: AtomicInteger = AtomicInteger(users.size - 1)


    fun getUsers(): Collection<User> {
        return users.values
    }

    fun addUser(user: User) {
        val id = lastId.getAndIncrement()
        user.id = id
        users.put(id, user)
    }

    fun updateUser(user: User) {
        users.put(user.id, user)
    }

    fun checkIfUserEmailExists(email: String) : Boolean {
        return getUsers().any { it.email == email }
    }

    fun getUser(userId: Int): User {
        if (!users.containsKey(userId))
            throw IllegalArgumentException()
        return users[userId]!!
    }

    fun getUserWithEmail(email: String): User {
        return getUsers().find { it.email == email }!!
    }

    fun hasUserWithSub(sub: String): Boolean {
        return getUsers().any { it.sub == sub }
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