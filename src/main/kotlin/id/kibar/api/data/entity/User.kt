package id.kibar.api.data.entity

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)