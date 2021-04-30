package me.anmolverma.services.users

import kotlinx.coroutines.Dispatchers
import me.anmolverma.domain.AppUser
import org.litote.kmongo.coroutine.*

class UsersService(private val mongoCollection: CoroutineCollection<AppUser>) :
    UsersServiceGrpcKt.UsersServiceCoroutineImplBase(Dispatchers.IO) {

    override suspend fun login(request: LoginRequest): AuthResponse {
        return AuthResponse.newBuilder().setJwtToken("hwt_token").setRefreshToken("refresh").build()
    }

    override suspend fun getAllUsers(request: Empty): UsersResponse {
        return super.getAllUsers(request)
    }

    override suspend fun createUser(request: CreateUserDto): UserResponse {
        return super.createUser(request)
    }

    override suspend fun getUserById(request: UserFindById): UserResponse {
        return super.getUserById(request)
    }

    override suspend fun updateUser(request: UpdateUserDto): UserResponse {
        return super.updateUser(request)
    }
}
