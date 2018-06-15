package parsing

trait ParseResult {
  def clientId: String
}

trait ParseRule[T <: ParseResult] {
  def parse(s: String): Option[T]
}
