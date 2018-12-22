package data

internal interface ObjectMapper<in From, out To> {
  fun from(source: From): To
}
