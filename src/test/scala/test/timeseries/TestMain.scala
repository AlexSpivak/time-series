package test.timeseries

import challenges.timeseries.{ResultDataPoint, DataPoint, TimeSeries}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.ListBuffer

/**
 * Some little behavior tests
 */
class TestMain extends WordSpec with Matchers with TimeSeries {

  val listBuffer = ListBuffer(
    DataPoint(1355270609, 1.80215),
    DataPoint(1355270621,	1.80185),
    DataPoint(1355270646,	1.80195),
    DataPoint(1355270702,	1.80225)
  )

  val listBufferMeasurements = ListBuffer(1.80215, 1.80185, 1.80195, 1.80225)

  "The TimeSeries" should {
    "return a DataPoint from string" in {
      stringToDp("1355270609\t1.80215") shouldBe DataPoint(1355270609, 1.80215)
    }

    "return a ListBuffer[Double] from ListBoufer[DataPoint]" in {
      generateMeasurements(listBuffer) shouldBe listBufferMeasurements
    }

    "return a ResultDataPoint" in {
      makeResultDataPoint(ListBuffer(1.80215), DataPoint(1355270609, 1.80215)) shouldBe
      ResultDataPoint(1355270609, 1.80215, 1, 1.80215, 1.80215, 1.80215)

      makeResultDataPoint(ListBuffer(1.80215, 1.80185), DataPoint(1355270609, 1.80215)) shouldBe
          ResultDataPoint(1355270609, 1.80215, 2, 3.604, 1.80185, 1.80215)
    }

    "return an Iterator[ResultDataPoint] from Iterator[DataPoint]" in {
      val calculateList = calculateResult(listBuffer.toIterator).toList

      val resultList = List(
        ResultDataPoint(1355270609, 1.80215, 1, 1.80215, 1.80215, 1.80215),
        ResultDataPoint(1355270621, 1.80185, 2, 3.60400, 1.80185, 1.80215),
        ResultDataPoint(1355270646, 1.80195, 3, 5.40595, 1.80185, 1.80215),
        ResultDataPoint(1355270702, 1.80225, 2, 3.60420, 1.80195, 1.80225))

      calculateList.map(_.t) shouldBe resultList.map(_.t)
      calculateList.map(_.v) shouldBe resultList.map(_.v)
      calculateList.map(_.n) shouldBe resultList.map(_.n)
      calculateList.map(rdp => {
        Math.round(rdp.rs * 100000.0) / 100000.0
      }) shouldBe resultList.map(_.rs)
      calculateList.map(_.maxV) shouldBe resultList.map(_.maxV)
      calculateList.map(_.minV) shouldBe resultList.map(_.minV)

    }
  }
}

