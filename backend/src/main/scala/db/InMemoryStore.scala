package db

import model.*
import cats.effect.IO

import types.Types.Id

/**
  * Class that implements an in-memory data catalog for managing items in the application. It acts as a dummy for a real
  * database, allowing to showcase the functionality of Tapir endpoints without needing to set up a persistent 
  * storage solution. 
  * 
  * @author Eva Ray
  */
class InMemoryStore():

  /** Internal mutable state of the in-memory store, represented as a map from item IDs to items. It is initialized
   * with some seed data defined in the companion object. 
   */
  private var store: Map[Id, Item] = InMemoryStore.seedData

  /**
    * Retrieves all items currently stored in the in-memory catalog.
    *
    * @return An IO effect that, when executed, will produce a list of all items in the catalog.
    */
  def findAll: IO[List[Item]] =
    IO(store.values.toList)

  /**
    * Finds an item by its unique identifier.
    *
    * @param id The unique identifier of the item to find.
    * @return An IO effect that, when executed, will produce either an error or the found item.
    */
  def findById(id: Id): IO[Either[ApiError, Item]] =
    IO(store.get(id).toRight(NotFound(id)))

  /**
    * Creates a new item in the in-memory catalog.
    *
    * @param item The item to create.
    * @return An IO effect that, when executed, will produce either an error or the created item.
    */
  def create(item: Item): IO[Either[CreationError, Item]] =
    IO {
      if store.contains(item.id) then Left(ItemAlreadyExists(item.id))
      else
        store = store + (item.id -> item)
        Right(item)
    }

  /**
    * Deletes an item from the in-memory catalog by its unique identifier.
    *
    * @param id The unique identifier of the item to delete.
    * @return An IO effect that, when executed, will produce either an error or a unit value indicating success.
    */
  def delete(id: Id): IO[Either[ApiError, Unit]] =
    IO {
      if store.contains(id) then
        store = store - id
        Right(())
      else
        Left(NotFound(id))
    }

/**
  * Companion object for the InMemoryStore class, providing a factory method to create an instance of InMemoryStore and
  * defining some seed data to initialize the in-memory catalog with.
  * 
  * @author Eva Ray
  */
object InMemoryStore:

  /** Seed data to initialize the in-memory catalog with some predefined items. It is a map where the key is the item 
   * ID and the value is the Item object.
   */
  val seedData: Map[Id, Item] = Map(
    "b1"  -> Book("b1", "Le Seigneur des Anneaux", 31.9, List("Tolkien")),
    "b2"  -> Book("b2", "L'étranger", 19.9, List("Camus")),
    "cd1" -> CD("cd1", "Dark Side of the Moon", 19.9, List("Pink Floyd")),
    "vg1" -> VideoGame("vg1", "Zelda BOTW", 69.9, "Nintendo EAD", "Nintendo"),
  )

  /**
    * Factory method to create an instance of InMemoryStore. It initializes the in-memory catalog with the predefined seed data.
    * 
    * @return An IO effect that, when executed, will produce a new instance of InMemoryStore initialized with the seed data.
    */
  def make: IO[InMemoryStore] =
    IO(new InMemoryStore())