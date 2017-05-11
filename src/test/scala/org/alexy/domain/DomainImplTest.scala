package org.alexy.domain

import java.time.LocalDate.of

import org.alexy.TestHelper
import org.alexy.models.Row
import org.alexy.utils.{DataSource, Parser}
import org.specs2.matcher.ValueCheck
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeEach
/**
  * Created by alex on 11.05.17.
  */
class DomainImplTest extends Specification with Mockito with BeforeEach with TestHelper {
  var mockedParser: Parser = _
  val dummyDataSource: DataSource = null
  val domainTest = new DomainImpl(mockedParser)(dummyDataSource)

  override protected def before: Any = {
    mockedParser = mock[Parser]
    mockedParser.getDataBy(ticker)(dummyDataSource) returns parsedData
  }

  "Domain specification" >> {
    "should return - 1 year historic prices given a ticker" >> {
      domainTest.dailyPrices(ticker) mustEqual Seq(120.00, 140.00, 120.00, 200.00, 120.00)
    }

    "should return - 1 year daily returns given a ticker" >> {
      //, where return = ( Price_Today – Price_Yesterday)/Price_Yesterday /"
      val expected: Seq[ValueCheck[Double]] = Seq(0.166666667, 0.428571429, -0.4, 0).map(x => beCloseTo(x +/- delta))
      domainTest.returns(ticker) must contain(exactly(expected:_ *)).inOrder
    }

    "should return – 1 year mean returns given a ticker" >> {
      domainTest.meanReturn(ticker) must beCloseTo(140.00 +/- delta)
    }

    "should return empty daily prices returns on empty data" >> {
      domainTest.dailyPrices(ticker) mustEqual Seq.empty[Double]
    }

    "should return empty daily returns on empty data" >> {
      domainTest.returns(ticker) mustEqual Seq.empty[Double]
    }

    "should return empty daily returns on one row of data" >> {
      val youngTicker = "YNG"
      val result = Seq(Row(of(2016, 11, 1), 100.00, 120.00, 110.00, 120.00, 1000, 120.00))
      mockedParser.getDataBy(youngTicker)(dummyDataSource) returns result
      domainTest.returns(youngTicker) mustEqual Seq.empty[Double]
    }

    "should return zero mean returns on empty data" >> {
      domainTest.meanReturn(ticker) mustEqual 0d
    }

  }

}
