package id.kibar.api.data.service

import id.kibar.api.data.entity.User
import id.kibar.api.data.persistence.ActivityPersistence
import id.kibar.api.data.persistence.UserActivityPersistence
import id.kibar.api.data.persistence.UserPersistence

class RegistrationService {

    private val userPersistence = UserPersistence()
    private val userActivityPersistence = UserActivityPersistence()
    private val activityPersistence = ActivityPersistence()

    fun addNewUser(name: String, email: String, sub: String = "N/A") {
        this.save(User(firstName = name, email = email, lastName = name, sub = sub))
    }

    fun findByEmail(email: String): User? {
        val users = userPersistence.getUsers()
        return users.find { it.email == email }
    }

    fun save(user: User) {
        if (user.id == 0 && getUserIdForEmail(user.email) == -1)
            userPersistence.addUser(user)
        else
            userPersistence.updateUser(user)
    }

    fun checkIn(userId: Int, activityId: Int): String {
        val user = userPersistence.getUser(userId)
        val activity = activityPersistence.getActivity(activityId)
        return userActivityPersistence.checkInUser(user, activity)
    }

    fun getUserIdForEmail(email: String): Int {
        if (!userPersistence.checkIfUserEmailExists(email))
            return -1
        return userPersistence.getUserWithEmail(email).id
    }

    fun registerUserForActivity(userId: Int, activityId: Int): String {
        val user = userPersistence.getUser(userId)
        val activity = activityPersistence.getActivity(activityId)
        return userActivityPersistence.registerUser(user, activity)
    }

    fun signInUserIsNew(name: String, email: String, sub: String): Boolean {
        return if (!userPersistence.hasUserWithSub(sub)) {
            addNewUser(name = "", email = email, sub = sub)
            true
        } else {
            false
        }

    }

}

