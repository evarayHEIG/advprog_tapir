package routes

import service.OrderService
import endpoints.OrderEndpoints

import cats.effect.IO

/**
  * Routes for handling order-related HTTP requests.
  *
  * @author Eva Ray
  */
class OrderRoutes(orderService: OrderService):
    
    /**
      * Route to retrieve all orders in the system. It uses the getOrdersEndpoint defined in OrderEndpoints and maps 
      * the service response to the expected output format.
      */
    val getOrdersRoute = OrderEndpoints.getOrdersEndpoint.serverLogic { _ =>
        orderService.findAll.map(Right(_))
    }

    /**
      * Route to retrieve a specific order by its unique identifier. It uses the getOrderEndpoint defined in OrderEndpoints and maps 
      * the service response to the expected output format.
      */
    val getOrderRoute = OrderEndpoints.getOrderEndpoint.serverLogic { id =>
        orderService.findById(id).map {
            case Right(order) => Right(order)
            case Left(error)  => Left(error)
        }
    }

    /**
      * Route to create a new order. It uses the createOrderEndpoint defined in OrderEndpoints and maps 
      * the service response to the expected output format.
      */
    val createOrderRoute = OrderEndpoints.createOrderEndpoint.serverLogic { itemIds =>
        orderService.create(itemIds).map {
            case Right(order) => Right(order)
            case Left(error)  => Left(error)
        }
    }

    val all = List(getOrdersRoute, getOrderRoute, createOrderRoute)
