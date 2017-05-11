package org.alexy.domain

import org.alexy.utils.{Source, SourceImpl}

/**
  * Created by alex on 11.05.17.
  */
class Domain(parser: Source) {
  //  1 - 1 year historic prices given a ticker /
  def dailyPrices(ticker: String): Array[Double] = parser.getDataBy(ticker).map(_.close)

  //  2- daily returns, where return = ( Price_Today – Price_Yesterday)/Price_Yesterday /
  def returns(ticker: String) : Seq[Double] = {
    val sorted = parser.getDataBy(ticker).sortWith((a, b) => a.date.isBefore(b.date))
    (sorted zip sorted.tail).map { case (y, t) =>
      (t.close - y.close) / y.close
    }
  }

  // 3 – 1 year mean returns
  def meanReturn(ticker: String): Double = {
    val prices = dailyPrices(ticker)
    prices.sum / prices.length
  }
}
