import java.io.{BufferedWriter, FileWriter}

import org.apache.spark.sql._
import org.apache.spark.SparkFiles
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.sql.catalyst.encoders._

import java.io._

object Main {

  val DATASET_URL = "https://raw.githubusercontent.com/ilja2209/allert-buttons-analyzer/main/alert_button_dataset.csv"

  def determineAddress(points: List[Point]): Point = Utils.getGeometricMedian(points)

  def processGroup(key: String, values: Iterator[Row]): Row = {
    val points: List[Point] = values
      .map(v => new Point(v.getAs[Int]("x"), v.getAs[Int]("y")))
      .toList
    val address = determineAddress(points)
    Row(key, address.x, address.y)
  }

  def analyze(sparkSession: SparkSession, dataset: Dataset[Row], rowEncoder: ExpressionEncoder[Row]): Dataset[Row] = {
    val resultSchema = StructType(Array(
      StructField("device_id", StringType, nullable = false),
      StructField("x", IntegerType, nullable = false),
      StructField("y", IntegerType, nullable = false))
    )

    dataset
      .groupByKey(l => l.getAs[String]("device_id"))(Encoders.STRING)
      .mapGroups((k, v) => processGroup(k, v))(RowEncoder(resultSchema))
  }

  def prepareDataset(sparkSession: SparkSession, datasetPath: String): (Dataset[Row], ExpressionEncoder[Row]) = {
    val schema = StructType(Array(
      StructField("device_id", StringType, nullable = false),
      StructField("timestamp", IntegerType, nullable = true),
      StructField("x", IntegerType, nullable = true),
      StructField("y", IntegerType, nullable = true))
    )

    val rowEncoder = RowEncoder(schema)

    val dataset = sparkSession.read
      .format("csv")
      .option("header", value = true)
      .schema(schema)
      .csv(datasetPath)
      .as("device_id")

    Tuple2(dataset, rowEncoder)
  }

  def saveResultToFile(rdd: RDD[Row], path: String): Unit = {
    val lines = rdd.map(c => "%s;%s;%s\r\n".format(c(0), c(1), c(2)))
      .collect()
    val file = new File(path)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write("device_id;x;y\r\n")
    lines.foreach(line => bw.write(line))
    bw.close()
  }

  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession
      .builder
      .appName("Alert button tracking analyzer")
      .config("spark.master", "local")
      .getOrCreate()
    sparkSession.sparkContext.addFile(DATASET_URL)

    val dataset = prepareDataset(sparkSession, "file:///" + SparkFiles.get("alert_button_dataset.csv"))
    val resultDataset = analyze(sparkSession, dataset._1, dataset._2)
    val result = resultDataset
      .select("device_id", "x", "y")
      .rdd
    saveResultToFile(result, "result.csv")
    resultDataset.show(resultDataset.count().toInt)
    sparkSession.stop()
  }
}