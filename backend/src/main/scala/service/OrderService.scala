package service

import model.*
import db.*
import types.Types.Id

import cats.effect.IO
import cats.syntax.all.*

/**
  * Service class that provides business logic for managing orders in the application. It interacts with the 
  * InMemoryOrder to perform CRUD operations on orders and uses the ItemService to retrieve item details when 
  * creating orders.
  *
  * @param orderStore an instance of InMemoryOrder that serves as the data access layer for order-related operations
  * @param itemService an instance of ItemService that provides access to item-related operations
  * 
  * @author Eva Ray
  */
class OrderService(
        orderStore: InMemoryOrder,
        itemService: ItemService  
    ):

    /**
      * Retrieves all orders currently stored in the system. It simply delegates the call to the findAll 
      * method of the InMemoryOrder.
      *
      * @return An IO effect that, when executed, will produce a list of all orders in the system.
      */
    def findAll: IO[List[Order]] =
        orderStore.findAll

    /**
      * Finds a specific order in the system by its unique identifier. It delegates the call to the findById method of 
      * the InMemoryOrder and returns the result.
      *
      * @param id The unique identifier of the order to find.
      * @return An IO effect that, when executed, will produce either an error or the found order.
      */
    def findById(id: Id): IO[Either[ApiError, Order]] =
        orderStore.findById(id)

    /**
      * Creates a new order in the system. It retrieves the items by their IDs and calculates the total price.
      *
      * @param ids A list of unique identifiers for the items to include in the order.
      * @return An IO effect that, when executed, will produce either an error or the created order.
      */
    def create(ids: List[Id]): IO[Either[ApiError, Order]] =
        for
            itemResults <- ids.traverse(id => itemService.findById(id))
            result = itemResults.sequence match
                case Left(err)    => Left(err)
                case Right(items) =>
                    val total = items.map(_.price).sum
                    val order = Order(java.util.UUID.randomUUID().toString, items, total)
                    Right(order)
            finalResult <- result match
                case Left(err)    => IO.pure(Left(err))
                case Right(order) => orderStore.create(order)
        yield finalResult

    /**
      * Deletes an order from the system by its unique identifier. It delegates the call to the delete method of the 
      * InMemoryOrder and returns the result.
      *
      * @param id The unique identifier of the order to delete.
      * @return An IO effect that, when executed, will produce either an error or a unit value indicating success.
      */
    def delete(id: Id): IO[Either[ApiError, Unit]] =
        orderStore.delete(id)
