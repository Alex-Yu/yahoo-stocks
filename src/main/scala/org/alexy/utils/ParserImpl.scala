package org.alexy.utils

import org.alexy.models.Row

/**
  * Created by alex on 11.05.17.
  */
trait Parser {
  def getDataBy(ticker: String)(implicit ds: DataSource): Seq[Row]
}

class ParserImpl extends Parser {
  override def getDataBy(ticker: String)(implicit ds: DataSource): Seq[Row] =
    (ds.getUrl _ andThen ds.getRawFromUrl andThen parse)(ticker)

  private[this] def parse(rawData: Iterator[String]): Seq[Row] = {
    if (rawData.nonEmpty) rawData.drop(1).flatMap(Row.from).toSeq
    else Seq.empty[Row]
  }
}
