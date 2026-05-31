package endpoints;

import model.*
import types.Types.Id

import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import io.circe.generic.auto.*

/**
  * Object that defines the API endpoints for managing orders in the application.
  * 
  * @author Eva Ray
  */
object OrderEndpoints:

    val getOrdersEndpoint: Endpoint[Unit, Unit, ApiError, List[Order], Any] =
        endpoint
        .get
        .in("orders")
        .errorOut(jsonBody[ApiError])
        .out(jsonBody[List[Order]])

    val getOrderEndpoint: Endpoint[Unit, Id, ApiError, Order, Any] =
        endpoint
        .get
        .in("orders" / path[Id]("id"))
        .errorOut(jsonBody[ApiError])
        .out(jsonBody[Order])

    val createOrderEndpoint: Endpoint[Unit, List[Id], ApiError, Order, Any] =
        endpoint
            .post
            .in("orders")
            .in(jsonBody[List[Id]])
            .errorOut(jsonBody[ApiError])
            .out(jsonBody[Order])

    val all = List(getOrdersEndpoint, getOrderEndpoint, createOrderEndpoint)

