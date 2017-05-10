package org.alexy

/**
  * Created by alex on 10.05.17.
  */

import java.time._

import scala.io.Source
import scala.util.Try

case class Row(date: LocalDate, open: Double, high: Double, low: Double, close: Double, volume: Long, adjClose: Double)
object Row {
  def from(raw: String): Option[Row] = {
    val cols = raw.split(",")
    Try {
      Row(
        date = LocalDate.parse(cols(0).trim),
        open = cols(1).trim.toDouble,
        high = cols(2).trim.toDouble,
        low = cols(3).trim.toDouble,
        close = cols(4).trim.toDouble,
        volume = cols(5).trim.toLong,
        adjClose = cols(6).trim.toDouble
      )
    }.toOption
  }
}

/**
  * Questions:
  * 1. Caching?
  * 2. What price: close or adjClose?
  * 3. In def prices: today argument means businessDate?
  */
object Main extends App {
  import Utils._

  //  1 - 1 year historic prices given a ticker /
  def dailyPrices(ticker: String): Array[Double] = getDataBy(ticker).map(_.close)

  //  2- daily returns, where return = ( Price_Today – Price_Yesterday)/Price_Yesterday /
  def returns(ticker: String) : Seq[Double] = {
    val sorted = getDataBy(ticker).sortWith((a, b) => a.date.isBefore(b.date))
    sorted.foreach(println) //TODO: Remove this after testing
    (sorted zip sorted.tail).map { case (y, t) =>
      (t.close - y.close) / y.close
    }
  }

  // 3 – 1 year mean returns
  def meanReturn(ticker: String): Double = {
    val prices = dailyPrices(ticker)
    prices.sum / prices.length
  }

  // example usage
  val googleDailyPrices = dailyPrices("GOOG")
  val googleDailyReturns = returns("GOOG")
  val googleAverageReturns = meanReturn("GOOG")

  println("-----------------daily prices:")
  googleDailyPrices.foreach(println)
  println("-----------------returns:")
  googleDailyReturns.foreach(println)
  println(s"-----------------mean:    $googleAverageReturns")

}

object Utils {
  def getDataBy(ticker: String): Array[Row] = { //TODO think about some caching
    val url = pricesURL(LocalDate.now(), ticker)
    getParsedData(getRaw(url))
  }

  private def pricesURL(businessDate : LocalDate, ticker: String) : String = {
    val lastYear = businessDate.minusYears(1)
    f"http://real-chart.finance.yahoo.com/table.csv?s=$ticker" +
      f"&a=${lastYear.getMonthValue}&b=${lastYear.getDayOfMonth}&c=${lastYear.getYear}" +
      f"&d=${businessDate.getMonthValue}&e=${businessDate.getDayOfMonth}&f=${businessDate.getYear}&g=d&ignore=.csv"
  }

  private def getRaw(url: String): Iterator[String] = Source.fromURL(url).getLines()//TODO: Think about IOExceptions and BadRequests

  private def getParsedData(rawData: Iterator[String]): Array[Row] = { //TODO: Think about proper datatype for Array
    rawData.drop(1).flatMap(Row.from).toArray
  }
}

