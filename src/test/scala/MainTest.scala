import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.scalatest.flatspec.AnyFlatSpec

import scala.io.Source

class MainTest extends AnyFlatSpec {

  val SCHEMA = StructType(Array(
    StructField("device_id", StringType, nullable = false),
    StructField("timestamp", IntegerType, nullable = true),
    StructField("x", IntegerType, nullable = true),
    StructField("y", IntegerType, nullable = true))
  )

  "A processGroup" should "return address point for device_id and data from test_dataset_60634.csv" in {
    val device_id = 60634
    val rows = Source.fromResource("test_dataset_60634.csv").getLines()
      .map(_.split(";"))
      .map(getRow)
      .toList
    val expectedRow = Row("60634", 2727, 6402)
    val actualRow = Main.processGroup(device_id.toString, rows.iterator)
    assertResult(3)(expectedRow.size)
    (0 to 2).foreach(i => assertResult(expectedRow(i))(actualRow(i)))
  }

  "A processGroup" should "return address point for device_id and data from test_dataset.csv" in {
    val sparkSession = SparkSession
      .builder
      .appName("Simple Application")
      .config("spark.master", "local")
      .getOrCreate()
    val path = getClass.getResource("test_dataset.csv").getPath
    val ds = Main.prepareDataset(sparkSession, path)
    val actualDataSet = Main.analyze(sparkSession, ds._1, ds._2)
    val expectedRows = Array(Row("59727", 2974, 6014), Row("31296", 3280, 6248))
    val actualRows = actualDataSet.select("device_id", "x", "y").rdd.collect()
    assertResult(2)(actualRows.length)
    (0 to 2).foreach(i => assertResult(actualRows(0)(i))(expectedRows(0)(i)))
    (0 to 2).foreach(i => assertResult(actualRows(1)(i))(expectedRows(1)(i)))
  }

  def getRow(sArray: Array[String]): Row = {
    val values = sArray.map(Integer.parseInt).map(_.asInstanceOf[Any])
    new GenericRowWithSchema(values, SCHEMA)
  }
}
