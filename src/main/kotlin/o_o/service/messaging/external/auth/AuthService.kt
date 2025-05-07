package o_o.service.messaging.external.auth

import org.springframework.stereotype.Service

@Service
class AuthService {
  fun getByToken(token: String): UserDTO {
    return UserDTO(0, "", "", "", "")
  }

  fun getById(id: Long): UserDTO {
    return UserDTO(0, "", "", "", "")
  }
}