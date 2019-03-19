package kmeans

import scala.annotation.tailrec
import scala.collection._
import scala.util.Random

class KMeans {

  // Initializes the clusters by choosing initial points
  // Hint: Use the Seed to generate random numbers as in DataGenerator.scala.
  //       Select 'k' random points from the given points
  
  def initializeMeans(k: Int, points: Seq[Point], seed:Int): Seq[Point] = {
    //val l = Random.shuffle(points)
    //l.take(k)
    Random.shuffle(points).take(k)
    //points.tail.take(k) // takeing from index 1 and K elements in a list 

  }


  // Find the cluster ("means") that are closest to the point p. 
  // Hint: squareDistance is defined in Point

  def findClosest(p: Point, means: GenSeq[Point]): Point = {
    var closestMean = means.head
    
    for (mean <- means){
      if (p.squareDistance(mean) < p.squareDistance(closestMean)){
        closestMean = mean
      }
    }
    closestMean
  }

  // Cluster all points
  // Hint: All points must be assigned to a mean point.
  // Remember to handle empty cases.
  def classify(points: GenSeq[Point], means: GenSeq[Point]): GenMap[Point, GenSeq[Point]] = {
    val dict = points.groupBy(findClosest(_, means))
    // So iterate over means get (empty) list and return map
    means.map(mean => 
                mean -> 
                  dict.getOrElse(mean, 
                    GenSeq())).toMap
  }

  def findAverage(oldMean: Point, points: GenSeq[Point]): Point = points match {
    case x :: xs  => new Point(
      points.
      tail.
      foldLeft(
        points.
        head
        .dims)(
          (acc,v) => acc.zip(
              v.dims
            )
            .map(t => t._1 + t._2))
                .map(sums => sums / points.length
                  )
                )
    case _ => 
    oldMean
  }

  // Get average of classification (using findAverage function) and update old cluster

  def update(classified: GenMap[Point, GenSeq[Point]], oldMeans: GenSeq[Point]): GenSeq[Point] = {
    oldMeans.map(oldMean => findAverage(oldMean, classified(oldMean)))
  }

  // Check if the sum of the distance between the old and the new clusters are less than eta
  def converged(eta: Double)(oldMeans: GenSeq[Point], newMeans: GenSeq[Point]): Boolean = {
    (oldMeans zip newMeans).forall{
  case (oldMean, newMean) => oldMean.squareDistance(newMean) <=  eta
    }
  }
  // Clusters all points with initial cluster means
  // The function returns when the clusters converge less than eta
@tailrec
final def kMeans(points: GenSeq[Point],
                   means: GenSeq[Point],
                   eta: Double): GenSeq[Point] = {
    val classified = classify(points, means)
    val newMeans = update(classified, means)
    if ((!converged(eta)(means, newMeans))) {
      kMeans(points, newMeans, eta);
    } 
    else {newMeans}
  }
}
object KMeansRunner {
  // Times execution of function
  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) + "ns")
    result
  }

  // Loads dataset and executes kmeans
  def main(args: Array[String]) {
    // Prepare dataset
    val numPoints = 500000
    val numDimensions = 3
    val eta = 0.01
    val k = 3 // Number of clusters
    val seed = 7

    // You can choose between the two datasets in the comments below. 
    //val points = DataLoader.loadDataset(
    //  "src/main/resources/uber-pickups-ny-20140402.tsv",
    //  DataLoader.uber)
    //val points = DataLoader.loadDataset(
    //  "src/main/resources/hofstede-nonull-vsm.tsv",
    //  DataLoader.hofstede)
    // Or generate a random dataset
    val points = DataGenerator.genPoints(numPoints, numDimensions, seed) //Outcommon this 

    // Initialize a KMeans object and initialize means
    val kMeans = new KMeans()
    val means = kMeans.initializeMeans(k, points, seed)
    
    // ---------------------------- //

    // val testPoint = points.head
    // val closest = kMeans.findClosest(testPoint, means)
    //val classified = kMeans.classify(points, means)
    //val (key, value) = classified.head
    //var newMeans = update(classified,means)
    //var newMean = kMeans.findAverage(key, value)
    //val oldMean = means.head

    // ----------------------------------/
    
    // Execute kMeans sequentially and time the execution
    println("Sequential Version")
    time{
      kMeans.kMeans(points, means, eta)
    }
    println()
    val parPoints = points.par
    val parMeans = means.par
    // Execute kMeans in parallel and time the execution
    println("Parallel version")
    time{
      
      kMeans.kMeans(parPoints, parMeans, eta)
    }
    println()

  }

}