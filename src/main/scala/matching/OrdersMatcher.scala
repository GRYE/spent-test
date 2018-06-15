package matching

import model.Model.{Buy, Client, Order, Sell}

trait OrdersMatcher {
  def matchOrders(clientsState: Seq[Client], orders: Seq[Order]): List[Client]
}

class OrdersMatcherImpl extends OrdersMatcher {

  override def matchOrders(clients: Seq[Client], orders: Seq[Order]): List[Client] = {
    println(s"Starting matching. Total clients: ${clients.size}, total orders: ${orders.size}")

    def executeOrder(clientA: Client, clientB: Client, clientAOrder: Order): (Client, Client) = {
      val totalPrice = clientAOrder.price * clientAOrder.amount
      val assetType = clientAOrder.assetType
      val amount = clientAOrder.amount
      clientAOrder.opType match {
        case Sell => (
          clientA.copy(
            balance = clientA.balance + totalPrice,
            assetBalances = clientA.assetBalances.updated(assetType, clientA.assetBalances(assetType) - amount)
          ),
          clientB.copy(
            balance = clientB.balance - totalPrice,
            assetBalances = clientB.assetBalances.updated(assetType, clientB.assetBalances(assetType) + amount)
          )
        )
        case Buy => executeOrder(clientB, clientA, clientAOrder.copy(opType = Sell, clientId = clientB.clientId))
      }
    }

    val state = Map[String, Client](clients.map(client => (client.clientId, client)): _*)

    orders.foldLeft((state, Seq.empty[Order])) { case ((st, prevOrders), currentOrder) =>
      st.get(currentOrder.clientId) match {
        case None => (st, prevOrders)
        case Some(currentOrderClient) =>
          prevOrders.find(order =>
            order.amount == currentOrder.amount &&
            order.price == currentOrder.price &&
            order.assetType == currentOrder.assetType &&
            order.clientId != currentOrder.clientId &&
            order.opType != currentOrder.opType
          ) match {
            case Some(order) =>
              println(s"Order $currentOrder matched with $order")
              val otherOrderClient = st(order.clientId)
              val (updatedCurrentOrderClient, updatedOtherOrderClient) = executeOrder(currentOrderClient, otherOrderClient, currentOrder)
              val updatedState = st
                .updated(updatedCurrentOrderClient.clientId, updatedCurrentOrderClient)
                .updated(updatedOtherOrderClient.clientId, updatedOtherOrderClient)
              (updatedState, prevOrders.filter(_ != order))
            case None => (st, prevOrders :+ currentOrder)
          }
      }
    } match {
      case (newState, unmatchedOrders) =>
        println(s"${unmatchedOrders.size} orders left unmatched")
        newState.values.toList
    }
  }
}
