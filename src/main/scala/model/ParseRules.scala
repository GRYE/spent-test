package model

import model.Model._
import parsing.ParseRule

import scala.util.{Success, Try}

object ParseRules {

  implicit val parseClient = new ParseRule[Client] {
    override def parse(s: String): Option[Client] = {

      import cats.implicits._

      val splitted = s.split("\t")

      val clientIdTry = Success(splitted(0))
      val balanceTry: Try[Int] = Try(splitted(1).toInt)
      val assetsTry: Try[Map[AssetType, Int]] = Try(Map(
        A -> splitted(2).toInt,
        B ->splitted(3).toInt,
        C -> splitted(4).toInt,
        D -> splitted(5).toInt)
      )

      (clientIdTry, balanceTry, assetsTry).mapN(Client).toOption
    }
  }

  implicit val parseOrder = new ParseRule[Order] {
    override def parse(s: String): Option[Order] = {

      import cats.implicits._

      def parseAssetType(assetString: String): Option[AssetType] = assetString match {
        case "A" => Some(A)
        case "B" => Some(B)
        case "C" => Some(C)
        case "D" => Some(D)
        case _ => None
      }

      def parseOpType(opString: String): Option[OpType] = opString match {
        case "s" => Some(Sell)
        case "b" => Some(Buy)
        case _ => None
      }

      val splitted = s.split("\t")
      (Some(splitted(0)),
        parseOpType(splitted(1)),
        parseAssetType(splitted(2)),
        Try(splitted(3).toInt).toOption,
        Try(splitted(4).toInt).toOption
      ).mapN(Order)
    }
  }
}