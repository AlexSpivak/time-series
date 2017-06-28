package challenges.timeseries

/**
 * Main class takes param from args - a file path,
 * and print to output result data with metrics:
 * T — number of seconds since beginning of epoch at which rolling window ends.
 * V — measurement of price ratio at time T.
 * N — number of measurements in the window.
 * RS — a rolling sum of measurements in the window.
 * MinV — minimum price ratio in the window.
 * MaxV — maximum price ratio the window.
 * All data takes from file and evaluating by algorithm from Test task.
 */

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.{Success, Try}

/**
 * Start data point after splitting the line
 * @param timestamp epoch timestamp
 * @param measurement price ratio
 */
case class DataPoint(timestamp: Int, measurement: Double)

/**
 * Result data prints to standard output
 * @param t number of seconds since beginning of epoch at which rolling window ends
 * @param v measurement of price ratio at time T
 * @param n number of measurements in the window
 * @param rs a rolling sum of measurements in the window
 * @param minV minimum price ratio in the window
 * @param maxV maximum price ratio the window
 */
case class ResultDataPoint(t: Int, v: Double, n: Int, rs: Double, minV: Double, maxV: Double) {
  override def toString = "%d %.5f %d %.5f %.5f %.5f".format(t, v, n, rs, minV, maxV)
}

/**
 * All evaluation from file path to output are being in this trait
 */
trait TimeSeries {

  val ROLLING_TIME_WINDOW = 60

  /**
   * Before start process get filePath in Try block and checks if filepath was in args.
   * If it wasn't print alert
   * If it is was starting to read File
   * @param filePath Try[String] block
   */
  def startWithArgs(filePath: Try[String]):Unit = {
    filePath match {
      case Success(path) => readFile(path)
      case _ => println("The args is empty. Please, put in args existing filepath.")
    }
  }

  /**
   * Prints header of output. For pairs taken a length of items columns from first line
   */
  def printHeader():Unit = {
    val list = List(("T", 10), ("V", 7), ("N", 1), ("RS", 7), ("MaxV", 7), ("MinV", 7))
    val header = list.map(item => {
      item._1 + " " * (item._2 - item._1.length)
    }).mkString(" ")
    val line = "-" * (list.unzip._2.sum + list.length - 1)
    println(header)
    println(line)
  }


  /**
   * Run process if file exists, If not exists print alert
   * @param filePath String path to file
   */
  def readFile(filePath: String): Unit = {
    val source = Try(Source.fromFile(filePath))
    source match {
      case Success(fileSource) =>
        printHeader()
        process(fileSource)
      case _ => println("No such file or directory. Please, put in args existing filepath.")
    }
  }

  /**
   * Takes a string line from file and evaluating a DataPoint of timestamp and measurement
   * @param line String line from file
   * @return DataPoint
   */
  def stringToDp(line: String): DataPoint = {
    val items = line.split("\t")
    DataPoint(items(0).toInt, items(1).toDouble)
  }

  /**
   * Generates ListBuffer of measurements (Double) from list buffer of DataPoint for evaluating finish metrics
   * @param l ListBuffer of DataPoint
   * @return ListBuffer of measurements(Double)
   */
  def generateMeasurements(l: ListBuffer[DataPoint]):ListBuffer[Double] = {
    l.map(_.measurement)
  }

  /**
   * Generate finish data point with all metrics. Uses DataPoint with current timestamp and measurement,
   * and list of measurements for evaluating min, max, sum etc.
   * @param measurements ListBuffer of measuremsnt(Double)
   * @param dataPoint current DataPoint
   * @return finish metrics in ResultDataPoint class
   */
  def makeResultDataPoint(measurements: ListBuffer[Double], dataPoint: DataPoint): ResultDataPoint = {
    ResultDataPoint(
      dataPoint.timestamp,
      dataPoint.measurement,
      measurements.size,
      measurements.sum,
      measurements.min,
      measurements.max
    )
  }

  /**
   * Evaluate result data from Iterator of entry DataPoint
   * @param it takes entry DataPoint Iterator
   * @return Iterator of Result Data
   */
  def calculateResult(it: Iterator[DataPoint]): Iterator[ResultDataPoint] = {
    var listBuffer = ListBuffer[DataPoint]()
    it.map( item => {
      listBuffer += item
      listBuffer = listBuffer.filter(dp => dp.timestamp >= item.timestamp - ROLLING_TIME_WINDOW)
      val measurements = generateMeasurements(listBuffer)
      makeResultDataPoint(measurements, item)
    }
    )
  }

  /**
   * Process from file line by line using io.Source. Evaluate operations, and print result
   * @param source io.Source from file
   */
  def process(source: Source): Unit = {
    val l = source.getLines().map(stringToDp)
    calculateResult(l).foreach(println(_))
    source.close()
  }
}

/**
 * Main takes args and start process
 */
object Main extends App with TimeSeries {

  val filePath = Try(args(0))

  startWithArgs(filePath)
}
