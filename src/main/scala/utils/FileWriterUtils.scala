package utils

import model.Model._

object FileWriterUtils {

  def writeToFileAsString[T](fileName: String, objects: Seq[T])(implicit writer: StringWriter[T]): Unit = {
    import java.io._

    val pw = new PrintWriter(new File(fileName))
    try {
      objects.foreach(obj => pw.println(writer.write(obj)))
    } finally pw.close()
  }

}

trait StringWriter[T] {
  def write(obj: T): String
}

object StringWriters {

  implicit val clientWriter = new StringWriter[Client] {
    override def write(obj: Client): String =
      s"${obj.clientId}\t${obj.balance}\t${obj.assetBalances(A)}\t${obj.assetBalances(B)}\t${obj.assetBalances(C)}\t${obj.assetBalances(D)}"
  }

}
