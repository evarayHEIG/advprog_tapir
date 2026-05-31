package model

import types.Types.Id

/**
  * Sealed trait that represents possible errors that can occur in the application.
  * 
  * @author Eva Ray
  */
sealed trait ApiError

/**
  * Error indicating that a requested resource was not found, identified by its ID.
  *
  * @param id the unique identifier of the resource that was not found
  */
case class NotFound(id: Id) extends ApiError

/**
  * Error indicating that an unexpected server error occurred, with a message describing the cause.
  *
  * @param cause a string describing the cause of the server error
  */
case class ServerError(cause: String) extends ApiError

/**
  * Sealed trait that represents errors that can occur during the creation of resources in the application.
  * It extends ApiError to indicate that these are specific types of API errors related to resource creation.
  */
sealed trait CreationError extends ApiError

/**
  * Error indicating that an attempt was made to create a resource that already exists, identified by its ID.
  *
  * @param id the unique identifier of the resource that already exists
  */
case class ItemAlreadyExists(id: Id) extends CreationError

/**
  * Error indicating that a validation error occurred, with a message describing the cause.
  *
  * @param cause a string describing the cause of the validation error
  */
case class ValidationError(cause: String) extends CreationError

// case class ForbiddenError(cause: String) extends CreationError
