package o_o.service.messaging

enum class NotificationType constructor(val code: Int) {
  CHAT_MESSAGE(0),
  ;


  companion object {
    fun fromCode(code: Int): NotificationType {
      for (status in entries) {
        if (status.code == code) {
          return status
        }
      }
      throw UnsupportedOperationException(
        "The lang code $code is not supported!"
      )
    }
  }
}