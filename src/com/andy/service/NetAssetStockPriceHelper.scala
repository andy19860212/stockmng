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

  /**
   * 通过网络查询股票信息前的初始化表格
   * @return
   */
  def getInitialTableValues: Array[Array[Any]] = {
    val emptyArrayOfArrayOfAny = new Array[Array[Any]](0)
    (emptyArrayOfArrayOfAny /: symbolsAndUntis) { (data, element) =>
      val (ticker, units) = element
      data ++ Array(List(ticker, units, "?", "?").toArray)
    }
  }

  /**
   * 获取当前用户拥有的股票信息
   * @return
   */
  def getStockTableValues: Array[Array[Any]] = {
    val emptyArrayOfArrayOfAny = new Array[Array[Any]](0)
    (emptyArrayOfArrayOfAny /: symbolsAndUntis) { (data, element) =>
      val (ticker, units) = element
      data ++ Array(List(ticker, units).toArray)
    }
  }

  /**
   * 并发通过网络获取股票当前价格
   * @param updater
   * @return
   */
  def fetchPrice(updater: Actor) = actor {
    val caller = self
    symbolsAndUntis.keys.foreach { ticker =>
      actor {
        caller !(ticker, StockPriceFinder.getLatestClosingPrice(ticker))
      }
    }

    //计算总价格
    val netWorth = (0.0 /: (1 to symbolsAndUntis.size)) { (worth, index) =>
      receiveWithin(10000) {
        case (ticker: String, latestClosingPrice: Double) =>
          val units = symbolsAndUntis(ticker)
          val value = units * latestClosingPrice
          //更新相关记录
          updater !(ticker, units, latestClosingPrice, value)
          worth + value
        case TIMEOUT =>
          worth
      }
    }
    //更新总价格和更新时间
    updater ! netWorth
  }
}
