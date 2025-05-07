package o_o.service.messaging.dto.message

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import o_o.service.messaging.model.message.Message

data class MessageCreateUpdateDTO(
  val id: String?,
  val account_id: Long,
  val message: String?,
  val media: String?,
  val chat_id: String,
  var replies_to: String?
)

data class MessageDeleteDTO(
  val account_id: Long,
  val id: String
)

data class MessageDeleteInfoDTO(
  val parent_id: String?,
  val id: String
)

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type"
)
@JsonSubTypes(
  JsonSubTypes.Type(value = ParentMessageInfoDTO::class, name = "ParentMessageInfoDTO"),
  JsonSubTypes.Type(value = ChildMessageInfoDTO::class, name = "ParentMessageInfoDTO")
)
abstract class MessageInfoDTO {
  var id: String? = null
  var message: String? = null
  var sender_account_id: Long? = null
  var created: Long? = null
}

class ParentMessageInfoDTO: MessageInfoDTO() {
  var chat_id: String? = null
  var replies: List<ChildMessageInfoDTO>? = null

  fun toDTO(entity: Message): ParentMessageInfoDTO {
    id = entity.id
    message = entity.message
    sender_account_id = entity.senderAccountId
    chat_id = entity.chat?.id
    created = entity.createdDate?.time
    replies = entity.replies.map { ChildMessageInfoDTO().toDTO(it) }

    return this
  }
}

class ChildMessageInfoDTO: MessageInfoDTO() {
  var replies_to: ChildMessageInfoDTO? = null
  var replies_to_parent: String? = null

  fun toDTO(entity: Message): ChildMessageInfoDTO {
    id = entity.id
    message = entity.message
    sender_account_id = entity.senderAccountId
    created = entity.createdDate?.time
    replies_to = entity.repliesTo?.let { ChildMessageInfoDTO().toDTO(it) }
    replies_to_parent = entity.parentMessageId

    return this
  }
}