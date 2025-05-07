package o_o.service.consultation.base.dto

class LabelValueDTO {
  var label: String = ""
  var value: String = ""
  var reason: Boolean = false

  fun toDTO(value: String): LabelValueDTO {
    this.value = value
    this.label = value.lowercase().capitalize().replace("_", " ")
    return this
  }
}

class LabelValueLongDTO {
  var label: String = ""
  var value: Long? = null

  fun toDTO(label: String, value: Long?): LabelValueLongDTO {
    this.label = label
    this.value = value
    return this
  }
}
