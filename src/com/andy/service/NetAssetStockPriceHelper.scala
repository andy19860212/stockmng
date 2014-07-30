package com.andy.service

import com.andy.model.url.StockPriceFinder

import scala.actors._
import Actor._

/**
 * Created by wb-zhangwei01 on 2014/7/30.
 * 更新股票价格的actor
 */
object NetAssetStockPriceHelper {
  val symbolsAndUntis = StockPriceFinder.getTickersAndUnits

  def getInitialTableValues: Array[Array[Any]] = {
    val emptyArrayOfArrayOfAny = new Array[Array[Any]](0)
    (emptyArrayOfArrayOfAny /: symbolsAndUntis) { (data, element) =>
      val (ticker, units) = element
      data ++ Array(List(ticker, units, "?", "?").toArray)
    }
  }

  def fetchPrice(updater: Actor) = actor {
    val caller = self
    symbolsAndUntis.keys.foreach { ticker =>
      actor {
        caller !(ticker, StockPriceFinder.getLatestClosingPrice(ticker))
      }
    }


    val netWorth = (0.0 /: (1 to symbolsAndUntis.size)) { (worth, index) =>
      receiveWithin(10000) {
        case (ticker: String, latestClosingPrice: Double) =>
          val units = symbolsAndUntis(ticker)
          val value = units * latestClosingPrice
          updater !(ticker, units, latestClosingPrice, value)
          worth + value
        case TIMEOUT =>
          worth
      }
    }
    updater ! netWorth
  }
}
