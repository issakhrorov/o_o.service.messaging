package o_o.service.messaging.dto.chat

import o_o.service.messaging.model.chat.Chat


data class ChatCreateUpdateDTO(
  val id: String?,
  val user_id: Long
)

class ChatInfoDTO {
  var id: String? = null
//  var sender: UserShortInfoDTO? = null
//  var receiver: UserShortInfoDTO? = null

  fun toDTO(entity: Chat): ChatInfoDTO {
    this.id = entity.id
//    this.sender = UserShortInfoDTO().toDTO(entity.userTwo)
//    this.receiver = UserShortInfoDTO().toDTO(entity.userOne)

    return this
  }
}