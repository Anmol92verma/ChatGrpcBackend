package me.anmolverma

import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import me.anmolverma.auth.FirebaseUserAuthenticatorService
import me.anmolverma.services.chat.ChatService
import me.anmolverma.services.users.UsersService
import me.anmolverma.domain.DBUser
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object MainGrpcServer {

    @JvmStatic
    fun main(args: Array<String>) {

        val appDatabase = appDatabase()
        val server = NettyServerBuilder.forPort(443)
            /*.useTransportSecurity(
                // reference file
                File("./src/ssl/server.crt"),
                File("./src/ssl/server.pem")
            )*/
            .addService(UsersService(FirebaseUserAuthenticatorService()))
            .addService(ChatService())
            //.intercept(JWTInterceptor())
            .build()

        server.start()
        print("GRPC server on port ${server.port} ,started!")
        server.awaitTermination()
    }

    private fun userCollection(appDatabase: CoroutineDatabase) =
        appDatabase.getCollection<DBUser>("users")

    private fun appDatabase(): CoroutineDatabase {
        val client = KMongo.createClient(System.getenv("mongodbstring")).coroutine
        return client.getDatabase("whatsappdb")
    }

}