package org.alexy.utils

import java.time.LocalDate
import java.util.logging.Logger

import org.alexy.models.Row
import org.alexy.system.Config

import scala.util.Try

/**
  * Created by alex on 11.05.17.
  */
trait DataSource {
  def getDataBy(ticker: String): Seq[Row]
}

class DataSourceImpl(config: Config) extends DataSource {
  val logger: Logger = Logger.getLogger(this.getClass.getName)

  override def getDataBy(ticker: String): Seq[Row] = {
    val url = pricesURL(LocalDate.now(), ticker)
    (getRawFromUrl _ andThen getParsedData)(url)
  }

  private[this] def getRawFromUrl(url: String): Iterator[String] = Try {
    io.Source.fromURL(url).getLines()
  }.fold(
    ex => {
      logger.info(s"ERROR: $url is unreachable - ${ex.getMessage} is thrown")
      Iterator.empty
    }, identity
  )

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

  private[this] def getParsedData(rawData: Iterator[String]): Seq[Row] = {
    if (rawData.nonEmpty) rawData.drop(1).flatMap(Row.from).toSeq
    else Seq.empty[Row]
  }
}
