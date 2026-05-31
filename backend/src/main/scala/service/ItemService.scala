package service;

import model.*
import db.*
import types.Types.Id

import cats.effect.IO

/**
  * Service class that provides business logic for managing items in the application. It interacts with the 
  * InMemoryStore to perform CRUD operations on items.
  *
  * @param store an instance of InMemoryStore that serves as the data access layer for item-related operations
  * 
  * @author Eva Ray
  */
class ItemService(
    store: InMemoryStore
):

  /**
    * Retrieves all items currently available in the in-memory catalog. It simply delegates the call to the findAll 
    * method of the InMemoryStore.
    *
    * @return An IO effect that, when executed, will produce a list of all items in the catalog.
    */
  def findAll: IO[List[Item]] =
      store.findAll

  /**
    * Finds a specific item in the catalog by its unique identifier. It delegates the call to the findById method of 
    * the InMemoryStore and returns the result.
    *
    * @param id The unique identifier of the item to find.
    * @return An IO effect that, when executed, will produce either an error or the found item.
    */
  def findById(id: Id): IO[Either[ApiError, Item]] =
      store.findById(id)

  /**
    * Creates a new item in the catalog. It performs validation checks before creating the item.
    *
    * @param item The item to create.
    * @return An IO effect that, when executed, will produce either an error or the created item.
    */
  def create(item: Item): IO[Either[CreationError, Item]] =
    if item.title.isBlank then
      IO.pure(Left(ValidationError("title cannot be blank")))
    else if item.price < 0 then
      IO.pure(Left(ValidationError("price cannot be negative")))
    else
       store.create(item)

  /**
    * Deletes an item from the catalog by its unique identifier. It delegates the call to the delete method of the 
    * InMemoryStore and returns the result.
    *
    * @param id The unique identifier of the item to delete.
    * @return An IO effect that, when executed, will produce either an error or a unit value indicating success.
    */
  def delete(id: Id): IO[Either[ApiError, Unit]] =
      store.delete(id)
