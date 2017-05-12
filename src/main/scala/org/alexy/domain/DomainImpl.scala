package org.alexy.domain

import org.alexy.utils.{DataSource, Parser}

/**
  * Created by alex on 11.05.17.
  */
trait Domain {
  def dailyPrices(ticker: String): Seq[Double]

  def returns(ticker: String): Seq[Double]

  def meanReturn(ticker: String): Double
}

class DomainImpl(parser: Parser)(implicit dataSource: DataSource) extends Domain {

  override def dailyPrices(ticker: String): Seq[Double] = parser.getDataBy(ticker).map(_.close)

  override def returns(ticker: String) : Seq[Double] = {
    val data = parser.getDataBy(ticker)
    if (data.isEmpty) Seq.empty[Double] else {
      val sorted = parser.getDataBy(ticker).sortWith((a, b) => a.date.isBefore(b.date))
      (sorted zip sorted.tail).map { case (y, t) =>
        (t.close - y.close) / y.close
      }
    }
  }

  override def meanReturn(ticker: String): Double = {
    val ret = returns(ticker)
    if (ret.isEmpty) 0 else ret.sum / ret.length
  }
}
