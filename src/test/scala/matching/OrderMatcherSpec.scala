package matching

import model.Model.{Order, _}
import org.scalatest.{FlatSpec, Matchers}

class OrderMatcherSpec extends FlatSpec with Matchers {

  val matcher = new OrdersMatcherImpl
  val sampleClients = Seq(
    Client("C1", 1000, Map(A -> 20, B -> 40, C -> 30, D -> 10)),
    Client("C2", 1500, Map(A -> 10, B -> 15, C -> 0, D -> 10)),
    Client("C3", 900, Map(A -> 35, B -> 25, C -> 15, D -> 5))
  )

  val sampleOrders = Seq(
    Order("C1", Sell, A, 30, 10),
    Order("C2", Buy, A, 30, 10),
    Order("C3", Buy, B, 50, 50),
    Order("C2", Sell, D, 15, 7),
    Order("C1", Buy, D, 15, 7)
  )

  val afterMatchingClients = Seq(
    Client("C1", 1000 + 10 * 30 - 7 * 15, Map(A -> (20 - 10), B -> 40, C -> 30, D -> (10 + 7))),
    Client("C2", 1500 - 10 * 30 + 7 * 15, Map(A -> (10 + 10), B -> 15, C -> 0, D -> (10 - 7))),
    Client("C3", 900, Map(A -> 35, B -> 25, C -> 15, D -> 5))
  )

  "OrderMatcher" should "correctly match all orders" in {
    val newState = matcher.matchOrders(sampleClients, sampleOrders)

    newState.sortBy(_.clientId) shouldEqual afterMatchingClients
  }

}
