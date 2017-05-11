package org.alexy.parser

import org.alexy.TestHelper
import org.alexy.models.Row
import org.alexy.utils.{DataSource, ParserImpl}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeEach

/**
  * Created by alex on 11.05.17.
  */
class ParserImplTest extends Specification with Mockito with BeforeEach with TestHelper {
  var mockedDataSourceO: DataSource = _
  val parser = new ParserImpl

  override protected def before: Any = {
    mockedDataSourceO = mock[DataSource]
  }

  "Parser specification" >> {
    "should return parsed data by ticker" >> {
      mockedDataSourceO.getUrl(ticker) returns url
      mockedDataSourceO.getRawFromUrl(url) returns rawData
      parser.getDataBy(ticker)(mockedDataSourceO) mustEqual parsedData
    }

    "should return empty collection when get just headers" >> {
      mockedDataSourceO.getUrl(ticker) returns urlOnlyHeaders
      mockedDataSourceO.getRawFromUrl(urlOnlyHeaders) returns rawDataWithOnlyHeaders
      parser.getDataBy(ticker)(mockedDataSourceO) mustEqual Seq.empty[Row]
    }

    "should return collection without bad rows" >> {
      mockedDataSourceO.getUrl(ticker) returns urlBadRow
      mockedDataSourceO.getRawFromUrl(urlBadRow) returns rawDataWithBadRow
      parser.getDataBy(ticker)(mockedDataSourceO).size mustEqual rawDataWithBadRow.size - 2
    }
  }

}
