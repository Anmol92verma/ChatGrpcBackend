package me.anmolverma.domain

import me.anmolverma.services.users.RegistrationTypes
import org.bson.types.ObjectId

data class DBUser(
    var _id: ObjectId? = null,
    var userId: String? = null,
    var uname: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var registrationTypes: RegistrationTypes? = null
)