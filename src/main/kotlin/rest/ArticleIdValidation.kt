package rest

import org.bson.types.ObjectId
import utils.errors.ValidationError

val String?.asArticleId: String
    get() {
        if (this.isNullOrBlank()) {
            throw ValidationError("id" to "Is Invalid")
        }

        try {
            ObjectId(this)
        } catch (e: Exception) {
            throw ValidationError("id" to "Is Invalid")
        }

        return this
    }

val String?.asIdToValue: String
    get() {
        if (this.isNullOrBlank()) {
            throw ValidationError("id" to "Is Invalid")
        }

        try {
            val regex = Regex("""value=(.*)""")
            val match = regex.find(this)
            val idHex = match?.groupValues?.get(1).let {
                return it?.substring(0, it.length - 1) ?: this;
            }
        } catch (e: Exception) {
            throw ValidationError("id" to "Is Invalid")
        }

    }