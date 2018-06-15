import matching.OrdersMatcherImpl
import model.Model.{Client, Order}
import parsing.FileParserImpl
import model.ParseRules._
import utils.FileWriterUtils
import utils.StringWriters._

object Main extends App {
  println("Starting app...")
  val clientsFileName = "clients.txt"
  val ordersFileName = "orders.txt"
  val resultFileName = "./result.txt"

  val parser = new FileParserImpl
  val matcher = new OrdersMatcherImpl

  val clients = parser.loadAndParse[Client](clientsFileName)
  val orders = parser.loadAndParse[Order](ordersFileName)

  val clientsAfterMatching = matcher.matchOrders(clients, orders)
  FileWriterUtils.writeToFileAsString(resultFileName, clientsAfterMatching.sortBy(_.clientId))
}
