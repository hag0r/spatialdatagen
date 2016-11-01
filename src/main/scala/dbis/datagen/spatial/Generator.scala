package dbis.datagen.spatial

import scala.collection.JavaConverters._
import scopt.OptionParser
import java.nio.file.Path
import java.nio.file.Paths
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import dbis.setm.SETM._
import dbis.setm.SETM
import java.io.PrintStream

/**
 * A generator for spatial data. It can create a set of points and polygons with 
 * specified characteristics. 
 */
object Generator {
  
  /*
   * This ugly hack is used to suppress the annoying ETM monitor info message.
   */
  val filteredOut = new PrintStream(System.out) {
    override def println(l: String) = if (!l.startsWith("[INFO ]")) super.println(l)
  }
  System.setOut(filteredOut)
  
  
  val NUM_DEFAULT = 10
  val POLYPOINTS_DEFAULT = 100
  
  object GeomType extends Enumeration {
    type GeomType = Value
    val POINT, POLYGON= Value
  }
  
  
  
  def main(args: Array[String]) {
    
    
    
    
    // parse CLI arguments
    val params = Params.parseArgs(args)
    
    if(params.quiet)
      SETM.disable
    
    /*
     * This starts everything.
     * 
     * The print method will call take the elements produced by generate
     * and print them to stdout or file, depending on CLI settings
     */
    print(
      generate(params), // call the generate method to produce the elements 
      params.file       // target output, if set, write to file, otherwise write to stdout
    )
    
    if(!params.quiet)
      collect()
    
  }
  
  /**
   * Perform data generation using the characteristics specified in params
   * 
   * @param params The parameters for generation
   */
  def generate(params: Params): Iterator[String] = timing("generate") {
    
    require(params.num > 0, "num must be > 0")
    require(params.types.nonEmpty, s"types must not be empty")
    require(!params.minX.isNaN(), "min-x must be set" )
    require(!params.maxX.isNaN(), "max-x must be set" )
    require(params.maxX > params.minX, "maxX must be > minX")
    require(!params.minY.isNaN(), "min-y must be set" )
    require(!params.maxY.isNaN(), "max-y must be set" )
    require(params.maxY > params.minY, "maxY must be > minY")
    require((!params.types.contains(GeomType.POLYGON)) || params.maxXRadius > 0, "x-radius must be > 0")
    require((!params.types.contains(GeomType.POLYGON)) || params.maxYRadius > 0, "y-radius must be > 0")
    require((!params.types.contains(GeomType.POLYGON)) || params.polyPoints > 0, "y-radius must be > 0")
    
    val geoms = params.types.iterator.flatMap { t => 
      t match {
        case GeomType.POINT => 
          new PointGenerator(params.minX, params.maxX, params.minY, params.maxY, params.num).iterator
        case GeomType.POLYGON =>
          new PolygonGenerator(params.minX, params.maxX, params.minY, params.maxY, params.maxXRadius, params.maxYRadius,params.polyPoints, params.num).iterator
      }
    }.map(_.wkt)
    
    
    if(params.id)
      geoms.zipWithIndex.map{ case (wkt, id) => s"$id;$wkt" }
    else
      geoms
  }
  
  /**
   * Print elements to stdout or file 
   * 
   * @param elems The list of elements to print
   * @param file the file name to write to, if empty (None) use stdout 
   */
  def print(elems: Iterator[String], file: Option[Path]) = timing("print") {
    
    if(file.isDefined)
      Files.write(file.get, elems.toStream.asJava, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)
    else
      elems.foreach(println)
    
  }
  
}