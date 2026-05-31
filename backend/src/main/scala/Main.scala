import db.*
import service.*
import routes.*

import server.BackendServerConf
import cats.effect.{IO, IOApp}
import sttp.tapir.*
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

/**
  * Main object that serves as the entry point for the backend application. It initializes the in-memory data stores, 
  * services, and routes, and starts the HTTP server to handle incoming requests.
  *
  * @author Eva Ray
  */
object Main extends IOApp.Simple:

  /** 
    * The main method that initializes the application components and starts the server. It creates instances of the 
    * InMemoryStore and InMemoryOrder, initializes the ItemService and OrderService with the respective stores, sets up 
    * the routes for items and orders, and starts the backend server using the defined routes.
    *
    * @return An IO effect that, when executed, will start the server and keep it running.
    */
  override def run: IO[Unit] =
    for
      store        <- IO(new InMemoryStore())
      orders       <- IO(new InMemoryOrder())
      itemService   = ItemService(store)
      orderService  = OrderService(orders, itemService)
      routes        = ItemRoutes(itemService).all ++ OrderRoutes(orderService).all
      swaggerRoutes = SwaggerInterpreter().fromServerEndpoints[IO](routes, "Multimedia Store API", "1.0")
      httpApp       = Http4sServerInterpreter[IO]()
                        .toRoutes(routes ++ swaggerRoutes)
                        .orNotFound
      _            <- BackendServerConf.start(httpApp)
    yield ()