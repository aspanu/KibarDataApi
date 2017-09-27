package id.kibar.api.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import id.kibar.api.data.entity.User
import id.kibar.api.data.service.RegistrationService
import io.javalin.Javalin
import javax.xml.ws.http.HTTPException

fun main(args: Array<String>) {

    val regService = RegistrationService()

    val app = Javalin.create().port(7070).start()

    with(app) {

        get("/user/emailIdentification/:email") { ctx ->
            ctx.json(regService.getUserIdForEmail(ctx.param("email")!!))
        }

        post("/user/checkIn") { ctx ->
            regService.checkIn(ctx.queryParam("userId")!!.toInt(), ctx.queryParam("activityId")!!.toInt())
            ctx.status(204)
        }

        post("/user/activity/register") { ctx ->
            regService.registerUserForActivity(
                ctx.queryParam("userId")!!.toInt(),
                ctx.queryParam("activityId")!!.toInt()
            )
            ctx.status(204)
        }

        post("/user/signIn") { ctx ->
            val sub = ctx.formParam("sub") ?: throw HTTPException(400)
            val email = ctx.formParam("email") ?: throw HTTPException(400)
            val name = ctx.formParam("name") ?: ""
            val newUser = regService.signInUserIsNew(name = name, email = email, sub = sub)
            ctx.status(201).json(newUser)
        }

        post("/user/update") { ctx ->
            val mapper = ObjectMapper().registerKotlinModule()
            val user = mapper.readValue(ctx.body(), User::class.java)
            regService.save(user)
            ctx.status(200).json("Updated user: ${user.id} successfully")
        }
    }

}

