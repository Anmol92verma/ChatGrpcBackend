package me.anmolverma.auth

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import me.anmolverma.services.users.CreateUserDto
import java.io.FileInputStream
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.UserRecord
import me.anmolverma.services.users.PaginatedRequest
import com.google.firebase.auth.ExportedUserRecord


class FirebaseUserAuthenticatorService : IUserAuthenticatorService {

    init {
        loadAuthentication()
    }

    override fun loadAuthentication() {
        val serviceAccount = FileInputStream(System.getenv("firebaseServiceAccountPath"))
        val options: FirebaseOptions = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()
        FirebaseApp.initializeApp(options)
    }

    override fun getUser(uid: String): UserRecord? {
        return FirebaseAuth.getInstance().getUser(uid)
    }

    override fun getUsers(request: PaginatedRequest): Pair<String, List<ExportedUserRecord>> {
        val page = FirebaseAuth.getInstance().listUsers(null, request.limit)
        return Pair(page.nextPageToken,page.iterateAll().toList())
    }

    override fun createUser(createUserDto: CreateUserDto): UserRecord? {
        val request: UserRecord.CreateRequest = UserRecord.CreateRequest()
            .setEmail(createUserDto.email)
            .setEmailVerified(false)
            .setPassword(createUserDto.password)
            .setPhoneNumber(createUserDto.phoneNumber)
            .setDisplayName(createUserDto.userName)
            .setPhotoUrl(createUserDto.photoUrl)
            .setDisabled(false)
        val userRecord = FirebaseAuth.getInstance().createUser(request)
        println("Successfully created new user: " + userRecord.uid)
        return userRecord
    }

}