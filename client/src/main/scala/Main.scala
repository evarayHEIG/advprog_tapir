package client

import endpoints.*
import model.*
import cats.effect.{IO, IOApp}
import sttp.client3.httpclient.cats.HttpClientCatsBackend
import sttp.tapir.client.sttp.SttpClientInterpreter
import sttp.client3.UriContext

/**
  * Main object that serves as the entry point for the client application. It initializes the HTTP client, defines the 
  * API requests based on the endpoints, and executes a series of requests to demonstrate the functionality of the 
  * backend API.
  *
  * @author Eva Ray
  */
object Main extends IOApp.Simple:

  private val baseUrl = uri"http://localhost:8080"

  /** 
    * The main method that initializes the HTTP client.
    * 
    * @return An IO effect that, when executed, will perform the defined API requests and print the results to the console.
    */
  override def run: IO[Unit] =
    HttpClientCatsBackend.resource[IO]().use { backend =>
      val interp = SttpClientInterpreter()

      val getItem    = interp.toRequestThrowDecodeFailures(ItemEndpoints.getItemEndpoint, Some(baseUrl))
      val getAllItems = interp.toRequestThrowDecodeFailures(ItemEndpoints.getItemsEndpoint, Some(baseUrl))
      val createBook = interp.toRequestThrowDecodeFailures(ItemEndpoints.createBookEndpoint, Some(baseUrl))
      val createOrder = interp.toRequestThrowDecodeFailures(OrderEndpoints.createOrderEndpoint, Some(baseUrl))

      for
        // GET /items
        _ <- IO.println("\n--- GET all items ---")
        all <- backend.send(getAllItems(()))
        _ <- IO.println(s"Items: ${all.body}")

        // GET /items/b1 -> success
        _ <- IO.println("\n--- GET item b1 ---")
        found <- backend.send(getItem("b1"))
        _ <- IO.println(s"Found: ${found.body}")

        // GET /items/xxx -> NotFound
        _ <- IO.println("\n--- GET item xxx (not found) ---")
        notFound <- backend.send(getItem("xxx"))
        _ <- notFound.body match
          case Left(NotFound(id))  => IO.println(s"Expected error: item '$id' not found")
          case Left(err)           => IO.println(s"Other error: $err")
          case Right(item)         => IO.println(s"Got: $item")

        // POST /items/books
        _ <- IO.println("\n--- CREATE book ---")
        newBook = Book("b3", "Foundation", 24.9, List("Isaac Asimov"))
        created <- backend.send(createBook(newBook))
        _ <- IO.println(s"Created: ${created.body}")

        // POST /items/books
        _ <- IO.println("\n--- CREATE book ---")
        errorBook = Book("b4", "Dune", -2, List("Frank Herbert"))
        valError <- backend.send(createBook(errorBook))
        _ <- valError.body match
          case Left(err) =>
            err match
              case ValidationError(msg) => IO.println(s"Expected validation error: $msg")
              case ItemAlreadyExists(id) => IO.println(s"Expected error: item '$id' already exists")
          case Right(item) => IO.println(s"Got: $item")

        // POST /orders — avec ids valides
        _ <- IO.println("\n--- CREATE order ---")
        order <- backend.send(createOrder(List("b1", "cd1")))
        _ <- IO.println(s"Order: ${order.body}")

      yield ()
    }