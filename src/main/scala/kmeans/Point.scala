
package kmeans

// Class representing a point in a dataset
class Point(val dims: Seq[Double]){
  private def square(v: Double): Double = v * v

  // Returns the squared distance of this point to another point
  def squareDistance(that: Point): Double = 
    dims
      .zip(that.dims)
      .foldLeft(0.0)(
        (acc,v) => square(v._1 - v._2) + acc
        )
  private def round(v: Double): Double = (v * 100).toInt / 100.0
  override def toString = s"(${dims.map(round)})"

}

