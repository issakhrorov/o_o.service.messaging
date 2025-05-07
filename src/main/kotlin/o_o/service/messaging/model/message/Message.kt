package o_o.service.messaging.model.message

import o_o.service.messaging.base.model.BaseMongoDocument
import o_o.service.messaging.model.chat.Chat
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Message: BaseMongoDocument() {
  var message: String? = null

  var media: String? = null

  var senderAccountId: Long = 0

  var chat: Chat? = null

  var replies: MutableList<Message> = mutableListOf()

  var repliesTo: Message? = null

  var parentMessageId: String? = null

  var isRead: Boolean = false
}