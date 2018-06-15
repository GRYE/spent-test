package model

import parsing.ParseResult


object Model {
  final case class Client(clientId: String,
                          balance: Int,
                          assetBalances: Map[AssetType, Int]) extends ParseResult

  sealed trait OpType
  case object Sell extends OpType
  case object Buy extends OpType

  sealed trait AssetType
  case object A extends AssetType
  case object B extends AssetType
  case object C extends AssetType
  case object D extends AssetType

  final case class Order(clientId: String,
                         opType: OpType,
                         assetType: AssetType,
                         price: Int,
                         amount: Int) extends ParseResult
}
