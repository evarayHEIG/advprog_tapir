package model

import types.Types.Id

/**
 * Domain model for catalog items available in the application.
 *
 * This file defines the generic `Item` trait and concrete implementations
 * for different item types that can appear in the store: books, music CDs,
 * and video games.
 * 
 * @author Eva Ray
 */
sealed trait Item:
    /** Unique identifier for the item. */
    def id: Id

    /** Human-readable title of the item. */
    def title: String

    /** Retail price of the item. */
    def price: Double

/**
 * A book available in the catalog.
 *
 * @param id unique identifier of the book
 * @param title title of the book
 * @param price retail price of the book
 * @param author list of one or more authors
 */
case class Book(id: Id, title: String, price: Double, author: List[String]) extends Item

/**
 * A music CD available in the catalog.
 *
 * @param id unique identifier of the CD
 * @param title title of the album
 * @param price retail price of the CD
 * @param artist list of contributing artists
 */
case class CD(id: Id, title: String, price: Double, artist: List[String]) extends Item

/**
 * A video game available in the catalog.
 *
 * @param id unique identifier of the video game
 * @param title title of the game
 * @param price retail price of the game
 * @param developper development studio or individual (keeps original spelling)
 * @param publisher publishing company of the game
 */
case class VideoGame(id: Id, title: String, price: Double, developper: String, publisher: String) extends Item


