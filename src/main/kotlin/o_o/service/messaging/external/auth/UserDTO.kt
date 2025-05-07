package o_o.service.messaging.external.auth

data class UserDTO(
  val id: Long,
  val fullname: String,
  val username: String,
  val email: String,
  val avatar: String
)