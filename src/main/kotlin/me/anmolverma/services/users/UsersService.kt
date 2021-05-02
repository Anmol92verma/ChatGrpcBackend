package me.anmolverma.services.users

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import kotlinx.coroutines.Dispatchers
import me.anmolverma.auth.IUserAuthenticatorService

class UsersService(
    private val iUserAuthenticatorService: IUserAuthenticatorService
) : UsersServiceGrpcKt.UsersServiceCoroutineImplBase(Dispatchers.IO) {

    override suspend fun getUserById(request: UserFindById): UserResponse {
        val user = iUserAuthenticatorService.getUser(request.uid)
        return UserResponse.newBuilder()
            .setResponse(Response.newBuilder().build())
            .setUser(user?.toUser())
            .build()
    }

    override suspend fun getAllUsers(request: PaginatedRequest): UsersResponse {
        val usersTokenPair = iUserAuthenticatorService.getUsers(request)
        return UsersResponse.newBuilder()
            .setResponse(Response.newBuilder().build())
            .setNextToken(usersTokenPair.first)
            .addAllUsers(usersTokenPair.second.map {
                it.toUser()
            }).build()
    }

    override suspend fun createUser(request: CreateUserDto): Response {
        return try {
            iUserAuthenticatorService.createUser(request)
            Response.newBuilder()
                .setMessage("Success!")
                .setSuccess(true)
                .setCode(200)
                .build()
        } catch (ex: Exception) {
            Response.newBuilder()
                .setMessage(ex.message)
                .setCode(501)
                .build()
        } catch (ex: FirebaseAuthException) {
            Response.newBuilder()
                .setMessage(ex.message)
                .setCode(ex.httpResponse.statusCode)
                .build()
        }
    }
}

private fun UserRecord.toUser(): User {
    return User.newBuilder()
        .setUuid(this.uid)
        .setPhoneNumber(this.phoneNumber)
        .setEmail(this.email)
        .setPhotoUrl(this.photoUrl)
        .build()
}
