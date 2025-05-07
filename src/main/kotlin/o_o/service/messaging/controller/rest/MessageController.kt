package o_o.service.messaging.controller.rest

import jakarta.websocket.server.PathParam
import o_o.service.messaging.base.dto.pageable.PageableContentWithHeadersDTO
import o_o.service.messaging.dto.message.MessageInfoDTO
import o_o.service.messaging.dto.message.ParentMessageInfoDTO
import o_o.service.messaging.service.ws.message.MessageService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/\${api.path}/messages")
class MessageController(
  val service: MessageService,
) {
  @GetMapping("/")
  fun getAllPaged(
    @PathParam("chat_id") chat_id: String,
    principal: Principal,
    pageable: Pageable
  ): PageableContentWithHeadersDTO<List<MessageInfoDTO>> {
    val result = service.getAll(chat_id, pageable)

    return PageableContentWithHeadersDTO(
      null,
      hashMapOf(),
      result.content.map { ParentMessageInfoDTO().toDTO(it) },
      pageable,
      result.totalElements,
      result.isLast,
      result.totalPages
    )
  }

  @GetMapping("/all")
  fun getAll(@PathParam("chat_id") chat_id: String, principal: Principal): List<MessageInfoDTO> {
    val result = service.getAll(chat_id)

    return result.map { ParentMessageInfoDTO().toDTO(it) }
  }
}