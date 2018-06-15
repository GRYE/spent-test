package parsing

import scala.io.Source
import scala.util.{Failure, Success, Try}

trait FileParser {
  def loadAndParse[T <: ParseResult](resourceName: String)(implicit parseRule: ParseRule[T]): Seq[T]
}

class FileParserImpl extends FileParser {

  override def loadAndParse[T <: ParseResult](resourceName: String)(implicit parseRule: ParseRule[T]): Seq[T] = Try {
    println(s"Starting to parse resource $resourceName")
    val bufferedSource = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(resourceName))
    val result = bufferedSource.getLines().toList.flatMap(parseRule.parse)
    bufferedSource.close
    result
  } match {
    case Success(result) =>
      println(s"Successfully parsed $resourceName")
      result
    case Failure(th) =>
      println(s"Failed to parse $resourceName with error:")
      th.printStackTrace()
      Seq.empty[T]
  }

}
