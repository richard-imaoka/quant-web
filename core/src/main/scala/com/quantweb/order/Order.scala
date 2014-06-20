package com.quantweb.order

import org.joda.time.DateTime

/**
 * Created by nishyu on 2014/06/08.
 */
case class Order(
  assetName: String,
  price: FormattedNumber,
  quantity: FormattedNumber,
  buySell: BuySell,
  id: String,
  timeCreated: DateTime
)
