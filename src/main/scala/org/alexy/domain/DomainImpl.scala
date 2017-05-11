package org.alexy.domain

import org.alexy.utils.{DataSource, DataSourceImpl}

/**
  * Created by alex on 11.05.17.
  */
trait Domain {
  def dailyPrices(ticker: String): Array[Double]

  def returns(ticker: String) : Seq[Double]

  def meanReturn(ticker: String): Double
}

class DomainImpl(parser: DataSource) extends Domain {
  //  1 - 1 year historic prices given a ticker /
  override def dailyPrices(ticker: String): Array[Double] = parser.getDataBy(ticker).map(_.close)

  //  2- daily returns, where return = ( Price_Today – Price_Yesterday)/Price_Yesterday /
  override def returns(ticker: String) : Seq[Double] = {
    val sorted = parser.getDataBy(ticker).sortWith((a, b) => a.date.isBefore(b.date))
    (sorted zip sorted.tail).map { case (y, t) =>
      (t.close - y.close) / y.close
    }
  }

  // 3 – 1 year mean returns
  override def meanReturn(ticker: String): Double = {
    val prices = dailyPrices(ticker)
    prices.sum / prices.length
  }
}
