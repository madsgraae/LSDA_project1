package kmeans

import scala.io.Source
import java.io.IOException

object DataLoader {

    // Parser for the Uber dataset
    def uber(entry:String) : Point = {
        entry.split("\t").toList match {
            case _ :: x ::  y :: Nil => 
                new Point(Seq(x.toDouble,y.toDouble))
            case _ => throw new IOException("Dataformat is wrong")
        }
    }
    
    // Parser for the Hofstede dataset
    def hofstede(entry:String) : Point = {
        entry.split("\t").toList match {
            case _ :: x ::  y :: z :: xx :: yy :: zz :: Nil => 
                new Point(Seq(x.toDouble,y.toDouble, z.toDouble,
                    xx.toDouble, yy.toDouble, zz.toDouble ))
            case _ => throw new IOException("Dataformat is wrong")
        }
    }

    // Loads the dataset from path with the specified parser which is one of the functions above. 
    def loadDataset(path:String, parser:String => Point): Seq[Point] = {
        Source
            .fromFile(path)   // Open the specified file
            .getLines         // Get the lines from the file
            .toList           // Change the type to list
            .tail             // Remove the top line of the file passed
            .map(parser)      // Apply the parser to each line sequentially
    }
}


