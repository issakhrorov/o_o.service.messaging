package o_o.service.messaging.controller.rest

import o_o.service.messaging.dto.HTTPBooleanResponseDTO
import o_o.service.messaging.dto.chat.ChatInfoDTO
import o_o.service.messaging.external.auth.AuthService
import o_o.service.messaging.service.ws.chat.IChatService
import o_o.service.messaging.util.getTokenFromRequest
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/\${api.path:project/api/v1}/chat")
//@Api(description = "Чат", tags = ["Chat"])
class ChatController(
  val service: IChatService,
  val authService: AuthService
) {
  @GetMapping
  fun getAllChatsByUser(request: ServletServerHttpRequest): List<ChatInfoDTO> {
    val token = getTokenFromRequest(request)
    val user = authService.getByToken(token)
    return service.getAllByUser(user.id).map { ChatInfoDTO().toDTO(it) }
  }

  @DeleteMapping("/{id}")
  fun deleteChat(@PathVariable("id") id: String, request: ServletServerHttpRequest): HTTPBooleanResponseDTO {
    val token = getTokenFromRequest(request)
    val userId = authService.getByToken(token).id
    val entity = service.getById(id)
    service.delete(entity, userId.toString())

    return HTTPBooleanResponseDTO(true)
  }
}