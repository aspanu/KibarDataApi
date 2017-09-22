package id.kibar.api.data.dao

import id.kibar.api.data.entity.User
import java.util.concurrent.atomic.AtomicInteger

class UserDao {

    val users = hashMapOf(
            0 to User(firstName = "Alice", email = "alice@alice.kt", id = 0, password = "pass0", lastName = "Test"),
            1 to User(firstName = "Bob", email = "bob@bob.kt", id = 1, password = "pass1", lastName = "Test"),
            2 to User(firstName = "Carol", email = "carol@carol.kt", id = 2, password = "pass2", lastName = "Test"),
            3 to User(firstName = "Dave", email = "dave@dave.kt", id = 3, password = "pass3", lastName = "Test")
    )

    var lastId: AtomicInteger = AtomicInteger(users.size - 1)

    fun save(name: String, email: String) {
        val id = lastId.incrementAndGet()
        users.put(id, User(firstName = name, email = email, id = id, lastName = name, password = "pass$id"))
    }

    fun findById(id: Int): User? {
        return users[id]
    }

    fun findByEmail(email: String): User? {
        return users.values.find { it.email == email }
    }

    fun update(id: Int, user: User) {
        users.put(id,
            User(firstName = user.firstName, email = user.email, id = id, password = user.password, lastName = user.lastName)
        )
    }

    fun delete(id: Int) {
        users.remove(id)
    }

}