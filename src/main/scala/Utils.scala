object Utils {

  private val TEST_POINTS = Array(Array(-1.0, 0.0), Array(1.0, 0.0), Array(0.0, 1.0), Array(0.0, -1.0))

  // Geometric median algorithm: https://en.wikipedia.org/wiki/Geometric_median
  //Scala implementation of C++ implementation https://www.geeksforgeeks.org/geometric-median/
  def getGeometricMedian(points: List[Point]): Point = {
    var currentX = 0
    var currentY = 0

    points.foreach(point => {
      currentX += point.x
      currentY += point.y
    })

    currentX = currentX / points.size
    currentY = currentY / points.size

    var mind = euclideanDistance(new Point(currentX, currentY), points)

    points.foreach(point => {
      val newX = point.x
      val newY = point.y
      val d = euclideanDistance(new Point(newX, newY), points)
      if (d < mind) {
        mind = d
        currentX = newX
        currentY = newY
      }
    })

    var dist = 150
    val lowestLimit = 0.001

    while (dist > lowestLimit) {
      var flag: Boolean = false
      (0 to 3).foreach(k => {
        val newX = currentX + dist * TEST_POINTS(k)(0)
        val newY = currentY + dist * TEST_POINTS(k)(1)
        val d = euclideanDistance(new Point(newX.toInt, newY.toInt), points)
        if (d < mind && !flag) {
          mind = d
          currentX = newX.toInt
          currentY = newY.toInt
          flag = true
        }
      })
      if (!flag) {
        dist /= 2
      }
    }

    new Point(currentX, currentY)
  }

  private def euclideanDistance(point: Point, points: List[Point]): Double = points.map(p => euclideanDistance(point, p)).sum

  private def euclideanDistance(point1: Point, point2: Point): Double = {
    val x = Math.abs(point1.x - point2.x)
    val y = Math.abs(point1.y - point2.y)
    Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))
  }
}
