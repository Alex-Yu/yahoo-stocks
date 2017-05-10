package org.alexy.utils

import java.time.LocalDate

import org.alexy.models.Row
import org.alexy.system.Config

/**
  * Created by alex on 11.05.17.
  */
class Parser(getRawfromUrl: String => Iterator[String], config: Config) {
  def getDataBy(ticker: String): Array[Row] = { //TODO think about some caching
    val url = pricesURL(LocalDate.now(), ticker)   //TODO have to be mocked
    getParsedData(getRawfromUrl(url))
  }

  private[this] def pricesURL(businessDate : LocalDate, ticker: String) : String = {
    val lastYear = businessDate.minusYears(1)
    config.urlMacrosO.map( urlMacros =>
      urlMacros.replaceAll("\\{\\{TICKER\\}\\}", ticker)
        .replaceAll("\\{\\{LY_MONTH\\}\\}", lastYear.getMonthValue.toString)
        .replaceAll("\\{\\{LY_DAY\\}\\}", lastYear.getDayOfMonth.toString)
        .replaceAll("\\{\\{LY\\}\\}", lastYear.getYear.toString)
        .replaceAll("\\{\\{MONTH\\}\\}", businessDate.getMonthValue.toString)
        .replaceAll("\\{\\{DAY\\}\\}", businessDate.getDayOfMonth.toString)
        .replaceAll("\\{\\{YEAR\\}\\}", businessDate.getYear.toString)
    ).getOrElse(
      f"http://real-chart.finance.yahoo.com/table.csv?s=$ticker" +
        f"&a=${lastYear.getMonthValue}&b=${lastYear.getDayOfMonth}&c=${lastYear.getYear}" +
        f"&d=${businessDate.getMonthValue}&e=${businessDate.getDayOfMonth}&f=${businessDate.getYear}&g=d&ignore=.csv"
    )
  }

  private[this] def getParsedData(rawData: Iterator[String]): Array[Row] = { //TODO: Think about proper datatype for Array
    rawData.drop(1).flatMap(Row.from).toArray                          //TODO: Drop is not typesafe
  }
}
