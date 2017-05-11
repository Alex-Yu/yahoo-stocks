package org.alexy.Domain
import java.time.LocalDate

import org.specs2.mutable.Specification
import org.alexy.domain.DomainImpl
import org.alexy.models.Row
import org.alexy.utils.Parser
import org.specs2.matcher.{Matcher, ValueCheck}
import org.specs2.mock.Mockito
/**
  * Created by alex on 11.05.17.
  */
class DomainImplTest extends Specification with Mockito {
  val mockedParser: Parser = mock[Parser]
  val dummyDataSource = null
  val domainTest = new DomainImpl(mockedParser)(dummyDataSource)

  val testData = Seq(
    Row(LocalDate.of(2016, 11, 1), 100.00, 120.00, 110.00, 120.00, 1000, 120.00),
    Row(LocalDate.of(2016, 11, 2), 130.00, 150.00, 110.00, 140.00, 2000, 140.00),
    Row(LocalDate.of(2016, 11, 5), 50.00, 80.00, 50.00, 120.00, 10000, 120.00),
    Row(LocalDate.of(2016, 11, 3), 200.00, 250.00, 110.00, 200.00, 5000, 200.00),
    Row(LocalDate.of(2016, 11, 4), 100.00, 120.00, 110.00, 120.00, 1000, 120.00)
  )
  val ticker = "GOOG"
  val delta = 0.00000001
  mockedParser.getDataBy(ticker)(dummyDataSource) returns testData

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

  }

}
