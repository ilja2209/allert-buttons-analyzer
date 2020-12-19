import org.scalatest.flatspec.AnyFlatSpec

import scala.io.Source

class UtilsTest extends AnyFlatSpec  {
  "A getGeometricMedian" should "return geometric median point = [2,2] for points [1,1] and [3,3]" in {
    val expectedPoint = new Point(2, 2)
    val actualPoint = Utils.getGeometricMedian(List[Point](new Point(1, 1), new Point(3, 3)))
    assertResult(expectedPoint)(actualPoint)
  }

  "A getGeometricMedian" should "return geometric median point = [2,2] for points read from test_points.csv" in {
    val points = Source.fromResource("test_points.csv").getLines()
      .map(_.split(";"))
      .map(ss => new Point(Integer.parseInt(ss(0)), Integer.parseInt(ss(1))))
      .toList
    val expectedPoint = new Point(2703, 6258)
    val actualPoint = Utils.getGeometricMedian(points)
    assertResult(expectedPoint)(actualPoint)
  }
}
