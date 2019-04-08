package kmeans

import scala.util.Random

object DataGenerator {

	// Generates a single point.
    def genPoint(pointDimension:Int, r:Random) : Point = {
        new Point((0 until pointDimension).map(_ => r.nextDouble()))
    }

    // Generates a dataset with the specified number of points each with the specified number of dimensions.
    def genPoints(numberOfPoints:Int, pointDimension:Int, seed:Int): Seq[Point] = {
        val r = new Random(seed)
        (0 until numberOfPoints)
            .map(_ => genPoint(pointDimension, r))
    }
}


