package com.quantweb.order

import org.scalatest.{Matchers, FlatSpec}
import org.joda.time.DateTime

/**
 * Created by Richard Imaoka (richard.s.imaoka@gmail.com) on 2014/07/13.
 */
class OrderMatchStepResultTest extends FlatSpec with Matchers {

  "NoFill" should "return incoming and existing orders as they are, with no filled order" in {
    val buy = Order("assetA", 100, 10, Buy, "buy-order1", new DateTime())
    val sell = Order("assetA", 101, 10, Sell, "sell-order1", new DateTime())
    val noFill = NoFill(buy, sell)

    noFill.remainingIncomingOrder shouldEqual Some(buy)
    noFill.remainingExistingOrder shouldEqual Some(sell)
    noFill.filledOrder shouldEqual None
  }

  "IncomingFullyFilled_ExistingFullyFilled" should "have empty incoming and existing orders, with fully filled order" in {
    val sell = Order("assetA", 99, 10, Sell, "sell-order1", new DateTime())
    val buy = Order("assetA", 100, 10, Buy, "buy-order1", new DateTime())
    val result = IncomingFullyFilled_ExistingFullyFilled(sell, buy)

    result.remainingIncomingOrder shouldEqual None
    result.remainingExistingOrder shouldEqual None
    result.filledOrder shouldEqual Some(FilledOrder(sell, buy, 100, 10))
  }

  "IncomingFullyFilled_ExistingPartiallyFilled" should "have empty incoming & remaining existing orders, with some filled order" in {
    val t = new DateTime()
    val sell = Order("assetA", 99, 10, Sell, "sell-order1", t)
    val buy = Order("assetA", 100, 20, Buy, "buy-order1", t)
    val result = IncomingFullyFilled_ExistingPartiallyFilled(sell, buy, 100, 10)

    result.remainingIncomingOrder shouldEqual None
    result.remainingExistingOrder shouldEqual Some(Order("assetA", 100, 10, Buy, "buy-order1", t))
    result.filledOrder shouldEqual Some(FilledOrder(sell, buy, 100, 10))
  }

  "IncomingPartiallyFilled_ExistingFullyFilled" should "have remaining incoming & empty existing orders, with some filled order" in {
    val t = new DateTime()
    val sell = Order("assetA", 99, 30, Sell, "sell-order1", t)
    val buy = Order("assetA", 100, 15, Buy, "buy-order1", t)
    val result = IncomingPartiallyFilled_ExistingFullyFilled(sell, buy, 100, 15)

    result.remainingIncomingOrder shouldEqual Some(Order("assetA", 99, 15, Sell, "sell-order1", t))
    result.remainingExistingOrder shouldEqual None
    result.filledOrder shouldEqual Some(FilledOrder(sell, buy, 100, 15))
  }
}
