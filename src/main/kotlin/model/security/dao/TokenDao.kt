package model.security.dao

import org.apache.http.util.EntityUtils
import utils.env.Environment
import utils.env.Log
import utils.gson.jsonToObject
import utils.http.HttpTools

class TokenDao(val http: HttpTools) {
    fun retrieveUser(token: String): User? {
        return try {
            http.get(
                "${Environment.env.securityServerUrl}/v1/users/current",
                listOf("Authorization" to token)
            ).let {
                if (it.statusLine.statusCode != 200) {
                    println("Pego el endopoint NO OK")
                    println(EntityUtils.toString(it.entity))
                    return null
                }
                println("Pego el endopoint OK")
                it.entity ?: return null

                EntityUtils.toString(it.entity).jsonToObject<User>()
            }
        } catch (e: Exception) {
            Log.error(e)
            null
        }
    }
}