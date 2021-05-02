package me.anmolverma.auth

import com.google.firebase.auth.ExportedUserRecord
import com.google.firebase.auth.UserRecord
import me.anmolverma.services.users.CreateUserDto
import me.anmolverma.services.users.PaginatedRequest

interface IUserAuthenticatorService {
    fun loadAuthentication()
    fun createUser(createUserDto: CreateUserDto): UserRecord?
    fun getUsers(request: PaginatedRequest): Pair<String, List<ExportedUserRecord>>
    fun getUser(uid: String): UserRecord?
}
