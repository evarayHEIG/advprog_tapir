package routes

import service.ItemService
import endpoints.ItemEndpoints
import model.*

/**
  * Routes for handling item-related HTTP requests. It defines the logic for each endpoint related to items by using 
  * the ItemService to perform the necessary operations and mapping the results to the expected output format defined 
  * in ItemEndpoints.
  *
  * @param service an instance of ItemService that provides access to item-related operations
  * 
  * @author Eva Ray
  */
class ItemRoutes(service: ItemService):

  /** Route to retrieve all items in the catalog.**/ 
  val getItemsRoute = ItemEndpoints.getItemsEndpoint
    .serverLogic(_ => service.findAll.map(Right(_)))

  /** Route to retrieve a specific item by its unique identifier. **/
  val getItemRoute = ItemEndpoints.getItemEndpoint
    .serverLogic(id => service.findById(id))

  /** Route to create a new book item in the catalog. **/
  val createBookRoute = ItemEndpoints.createBookEndpoint
    .serverLogic((book: Book) => service.create(book))

  /** Route to create a new CD item in the catalog. **/
  val createCDRoute = ItemEndpoints.createCDEndpoint
    .serverLogic((cd: CD) => service.create(cd))

  /** Route to create a new video game item in the catalog. **/
  val createVideoGameRoute = ItemEndpoints.createVideoGameEndpoint
    .serverLogic((videoGame: VideoGame) => service.create(videoGame))

  /** List of all item-related routes defined in this class. **/
  val all = List(getItemsRoute, getItemRoute, createBookRoute, createCDRoute, createVideoGameRoute)

    