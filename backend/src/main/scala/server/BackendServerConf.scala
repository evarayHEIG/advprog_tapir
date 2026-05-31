package server

import cats.effect.IO
import com.comcast.ip4s.*
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder

/**
  * Object that provides a method to start the backend server for the application. It uses the Ember server from Http4s 
  * to create and run the server, which listens for incoming HTTP requests and serves responses based on the defined 
  * routes and endpoints.
  *
  * @author Eva Ray
  */
object BackendServerConf:

  /** 
    * Starts the backend server on the specified port. 
    * 
    * @param httpApp The HttpApp that defines how to handle incoming HTTP requests.
    * @param port The port number on which the server should listen for incoming requests (default is 8080).
    * @return An IO effect that, when executed, will start the server and keep it running.
    */
  def start(httpApp: HttpApp[IO], port: Int = 8080): IO[Unit] =
    val serverPort = Port.fromInt(port).getOrElse(port"8080")  // fallback propre
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(serverPort)
      .withHttpApp(httpApp)
      .build
      .use { server =>
        IO.println(s"Server started on port ${server.address.getPort}") *> IO.never
      }