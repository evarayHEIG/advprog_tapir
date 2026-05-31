package endpoints

import model.*
import types.Types.Id

import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import io.circe.generic.auto.*

/**
  * Object that defines the API endpoints for managing items in the application.
  *
  * @author Eva Ray
  */
object ItemEndpoints:
    
    /** Endpoint to retrieve the list of all items in the catalog.**/
    val getItemsEndpoint: Endpoint[Unit, Unit, ApiError, List[Item], Any] =
        endpoint
        .get
        .in("items")
        .errorOut(jsonBody[ApiError])
        .out(jsonBody[List[Item]])

    /** Endpoint to retrieve a specific item in the catalog by its unique identifier. **/
    val getItemEndpoint: Endpoint[Unit, Id, ApiError, Item, Any] =
        endpoint
        .get // méthode HTTP
        .in("items" / path[Id]("id")) // chemin + type de l'input
        .errorOut(jsonBody[ApiError]) // type de l'erreur
        .out(jsonBody[Item]) // type de la réponse en cas de succès

    /** Endpoint to create a new book item in the catalog. **/
    val createBookEndpoint: Endpoint[Unit, Book, CreationError, Item, Any] =
        endpoint
        .post
        .in("items" / "books")
        .in(jsonBody[Book])
        .errorOut(jsonBody[CreationError])
        .out(jsonBody[Item])

    /** Endpoint to create a new CD item in the catalog. **/
    val createCDEndpoint: Endpoint[Unit, CD, CreationError, Item, Any] =
        endpoint
        .post
        .in("items" / "cds")
        .in(jsonBody[CD])
        .errorOut(jsonBody[CreationError])
        .out(jsonBody[Item])

    /** Endpoint to create a new video game item in the catalog. **/
    val createVideoGameEndpoint: Endpoint[Unit, VideoGame, CreationError, Item, Any] =
        endpoint
        .post
        .in("items" / "videogames")
        .in(jsonBody[VideoGame])
        .errorOut(jsonBody[CreationError])
        .out(jsonBody[Item])

    /** List of all item-related endpoints defined in this object. **/
    val all = List(getItemsEndpoint, getItemEndpoint, createBookEndpoint, createCDEndpoint, createVideoGameEndpoint)

