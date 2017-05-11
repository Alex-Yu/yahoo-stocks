package org.alexy.parser

import java.time.LocalDate

import org.alexy.models.Row
import org.alexy.utils.{DataSource, ParserImpl}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeEach

/**
  * Created by alex on 11.05.17.
  */
class ParserImplTest extends Specification with Mockito with BeforeEach {
  var mockedDataSource: DataSource = null
  val parser = new ParserImpl
  val ticker = "GOOG"
  val url = "http://dummy.com"
  val urlOnlyHeaders = "http://empty.com"
  val urlBadRow = "http://partly-bad.com"

  val parsedData = Seq(
    Row(LocalDate.of(2016, 11, 1), 100.00, 120.00, 110.00, 120.00, 1000, 120.00),
    Row(LocalDate.of(2016, 11, 2), 130.00, 150.00, 110.00, 140.00, 2000, 140.00),
    Row(LocalDate.of(2016, 11, 5), 50.00, 80.00, 50.00, 120.00, 10000, 120.00),
    Row(LocalDate.of(2016, 11, 3), 200.00, 250.00, 110.00, 200.00, 5000, 200.00),
    Row(LocalDate.of(2016, 11, 4), 100.00, 120.00, 110.00, 120.00, 1000, 120.00)
  )

  val rawData = Iterator(
    "Date,Open,High,Low,Close,Volume,Adj Close",
    "2016-11-01,100.00,120.00,110.00,120.00,1000,120.00",
    "2016-11-02,130.00,150.00,110.00,140.00,2000,140.00",
    "2016-11-05,50.00,80.00,50.00,120.00,10000,120.00",
    "2016-11-03,200.00,250.00,110.00,200.00,5000,200.00",
    "2016-11-04,100.00,120.00,110.00,120.00,1000,120.00"
  )

  val rawDataWithOnlyHeaders = Iterator(
    "Date,Open,High,Low,Close,Volume,Adj Close"
  )

  val rawDataWithBadRow = Iterator(
    "Date,Open,High,Low,Close,Volume,Adj Close",
    "2016-11-01,100.00,120.00,110.00,120.00,1000,120.00",
    "2016-11-02,130-00,150.00,110.00,140.00,2000,140.00"
  )

  override protected def before: Any = {
    mockedDataSource = mock[DataSource]
  }


  "Parser specification" >> {
    "should return parsed data by ticker" >> {
      mockedDataSource.getUrl(ticker) returns url
      mockedDataSource.getRawFromUrl(url) returns rawData
      parser.getDataBy(ticker)(mockedDataSource) mustEqual parsedData
    }

    "should return empty collection when get just headers" >> {
      mockedDataSource.getUrl(ticker) returns urlOnlyHeaders
      mockedDataSource.getRawFromUrl(urlOnlyHeaders) returns rawDataWithOnlyHeaders
      parser.getDataBy(ticker)(mockedDataSource) mustEqual Seq.empty[Row]
    }

    "should return collection without bad rows" >> {
      mockedDataSource.getUrl(ticker) returns urlBadRow
      mockedDataSource.getRawFromUrl(urlBadRow) returns rawDataWithBadRow
      parser.getDataBy(ticker)(mockedDataSource).size mustEqual rawDataWithBadRow.size - 2
    }
  }

}
