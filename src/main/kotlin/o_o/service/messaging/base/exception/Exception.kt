package o_o.service.messaging.base.exception

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

var logger: Logger = LogManager.getLogger("LoggedExceptions")

fun LoggedError(ex: RuntimeException): RuntimeException {
  println("EXCEPTION LOG: ${ex.javaClass.simpleName} with message: ${ex.message}")
  logger.error("EXCEPTION LOG: ${ex.javaClass.simpleName} with message: ${ex.message}")
  return ex
}

fun LoggError(ex: RuntimeException) {
  println("EXCEPTION LOG: ${ex.javaClass.simpleName} with message: ${ex.message}")
  logger.error("EXCEPTION LOG: ${ex.javaClass.simpleName} with message: ${ex.message}")
}


fun LoggError(ex: Exception) {
  println("EXCEPTION LOG: ${ex.javaClass.simpleName} with message: ${ex.message}")
  logger.error("EXCEPTION LOG: ${ex.javaClass.simpleName} with message: ${ex.message}")
}


@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class BadRequestException : RuntimeException()

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class CustomBadRequestException(msg: String) : RuntimeException(msg)


@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class CustomInternalServerException(msg: String) : RuntimeException(msg)

@ResponseStatus(HttpStatus.CONFLICT)
class CustomConflictException(msg: String) : RuntimeException(msg)

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class InvalidJWTTokenException : RuntimeException("Expired or invalid JWT token!")

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class InvalidBodyException(msg: String) : RuntimeException(msg)

@ResponseStatus(code = HttpStatus.CONFLICT)
class EmailNotAvailableException : RuntimeException("Email is not available!")

@ResponseStatus(code = HttpStatus.CONFLICT)
class UsernameNotAvailableException : RuntimeException("Username is not available!")

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class EmptyFileException : RuntimeException("File is empty!")

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class EmptyIdException : RuntimeException("Id is required!")

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class CustomEmptyIdException(msg: String) : RuntimeException(msg)

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class FileNotUploadedException : RuntimeException("File not uploaded!")

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class InternalServerErrorException : RuntimeException()

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
class InvalidCredentialsException : RuntimeException("Username or password is incorrect!")

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
class InvalidTokenException(msg: String) : RuntimeException(msg)

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class NotFoundException : RuntimeException()

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class CustomEntryNotFoundException(msg: String) : RuntimeException(msg)

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class EntryNotFoundException : RuntimeException("Entry not found with provided ID")


@ResponseStatus(code = HttpStatus.CONFLICT)
class TicketAlreadyDelivered(msg: String) : RuntimeException(msg)

@ResponseStatus(code = HttpStatus.CONFLICT)
class NoTicketsLeftException(msg: String) : RuntimeException(msg)

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class EmptyContentException(msg: String) : RuntimeException(msg)

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class NullFileException : RuntimeException("File is null!")

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class PasswordValidationException(message: String) : RuntimeException(message)

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class NotCreatedOrUpdatedException(error: String) : RuntimeException(error)

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class UserNotFoundException() : RuntimeException("User not found!")

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class AccountNotFoundException() : RuntimeException("Account not found!")

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class CustomFileNotFoundException : RuntimeException("File not found!")

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class CustomFileNotCreatedException(msg: String) : RuntimeException(msg)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class IdNotFoundException : RuntimeException("Id not found!")

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CustomForbiddenException(msg: String) : RuntimeException(msg)

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
class FileFormatException(msg: String) : RuntimeException(msg)

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class EntityNotCreatedException(error: String) : RuntimeException(error)

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class QRCodeIsNotUniqueException : RuntimeException("QR Code is not unique")

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class EmptyImageException(error: String) : RuntimeException(error)

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
class AuthorizationException : RuntimeException("Need to authorize to access this resource")



//INPUT EXCEPTIONS
@ResponseStatus(code = HttpStatus.CONFLICT)
class EntryExistsException(msg: String) : RuntimeException(msg)

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class InvalidTypeException : RuntimeException("Input type is incorrect!")

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class CustomInputFieldErrorException(msg: String) : RuntimeException("Input Field error: $msg")

