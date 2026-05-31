package db

import model.*
import cats.effect.IO

/**
  * Class that represents an in-memory data store for customer orders in the application.
  * 
  * @author Eva Ray
  */
class InMemoryOrder():

    /**
      * Internal mutable state to hold the orders in memory. It is a map where the key is the order ID and the value is the Order object.
      */
    private var orders: Map[String, Order] = Map.empty

    def findAll: IO[List[Order]] =
        IO(orders.values.toList)

    /**
      * Finds an order by its unique identifier.
      *
      * @param id The unique identifier of the order to find.
      * @return An IO effect that, when executed, will produce either an error or the found order.
      */
    def findById(id: String): IO[Either[ApiError, Order]] =
        IO(orders.get(id).toRight(NotFound(id)))
    
    /**
      * Creates a new order.
      *
      * @param order The order to create.
      * @return An IO effect that, when executed, will produce either an error or the created order.
      */
    def create(order: Order): IO[Either[ApiError, Order]] =
        IO {
            if orders.contains(order.id) then Left(ItemAlreadyExists(order.id))
            else                orders = orders + (order.id -> order)
                Right(order)
        }
    
    /**
      * Deletes an order by its unique identifier.
      *
      * @param id The unique identifier of the order to delete.
      * @return An IO effect that, when executed, will produce either an error or a unit value indicating success.
      */
    def delete(id: String): IO[Either[ApiError, Unit]] =
        IO {
            if orders.contains(id) then
                orders = orders - id
                Right(())
            else                
                Left(NotFound(id))
        }

/**
  * Companion object for the InMemoryOrder class, providing a factory method to create an instance of InMemoryOrder.
  * 
  * @author Eva Ray
  */
object InMemoryOrder:

    /**
      * Factory method to create an instance of InMemoryOrder. It initializes the in-memory order store with an empty map.
      * 
      * @return An IO effect that, when executed, will produce a new instance of InMemoryOrder initialized with an empty order store.
      */
    def make: IO[InMemoryOrder] =
        IO(new InMemoryOrder())

    


