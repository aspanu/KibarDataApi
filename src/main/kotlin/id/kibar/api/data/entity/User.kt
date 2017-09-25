package id.kibar.api.data.entity

data class User(
    var id: Int = 0,
    val email: String,
    val firstName: String,
    val lastName: String,
    var sub: String = "N/A" // The unique Id provided by google from the Oauth process
)