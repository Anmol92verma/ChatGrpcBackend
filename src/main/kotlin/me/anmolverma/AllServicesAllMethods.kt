package me.anmolverma

import io.grpc.CallCredentials
import io.grpc.ManagedChannel
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import kotlin.Throws
import java.lang.InterruptedException
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import me.anmolverma.MainGrpcClient.buildClientSslContext
import me.anmolverma.chat.ChatServiceGrpc
import me.anmolverma.services.users.CreateUserDto
import me.anmolverma.services.users.User
import me.anmolverma.services.users.UsersServiceGrpc


/**
 * An authenticating client that requests a product from the [MainGrpcServer].
 */
class AllServicesAllMethods internal constructor(
    private val callCredentials: CallCredentials?,
    private val channel: ManagedChannel
) {
    private val productServiceBlockingStub: ChatServiceGrpc.ChatServiceBlockingStub =
        ChatServiceGrpc.newBlockingStub(channel)
    private val userServiceBlockingStub: UsersServiceGrpc.UsersServiceBlockingStub =
        UsersServiceGrpc.newBlockingStub(channel)

    /**
     * Construct client for accessing GreeterGrpc server.
     */

    internal constructor(callCredentials: CallCredentials?, host: String?, port: Int) : this(
        callCredentials,
        NettyChannelBuilder.forAddress(host, port)
            .sslContext(buildClientSslContext())
            .build()
    )


    @Throws(InterruptedException::class)
    fun shutdown() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    /**
     * Say hello to server.
     *
     * @return the message in the HelloReply from the server
     */
    fun createGroup(logger: Logger): User? {
        // Use a stub with the given call credentials applied to invoke the RPC.
        val response = userServiceBlockingStub
            .withCallCredentials(callCredentials)
            .createUser(
                CreateUserDto.newBuilder()
                    .setUserName("anmol92verma")
                    .setEmail("anmol.verma4@gmail.com")
                    .build()
            )
        return response.user
    }

}

