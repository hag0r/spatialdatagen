package dbis.datagen

import scala.collection.JavaConverters._
import java.nio.file.Path
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import dbis.setm.SETM._
import dbis.setm.SETM
import java.io.PrintStream
import dbis.datagen.st.IntervalGenerator
import dbis.datagen.st.PointGenerator
import dbis.datagen.st.PolygonGenerator

/**
 * A generator for spatial data. It can create a set of points and polygons with 
 * specified characteristics. 
 */
object Generator {
  
  /*
   * This ugly hack is used to suppress the annoying ETM monitor info message.
   */
  val filteredOut = new PrintStream(System.out) {
    override def println(l: String) = if(!l.startsWith("[INFO ]")) super.println(l)
  }
  System.setOut(filteredOut)
  
  
  val NUM_DEFAULT = 10
  val POLYPOINTS_DEFAULT = 100
  
  object Type extends Enumeration {
    type Type = Value
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
    require((!params.types.contains(Type.POLYGON)) || params.maxXRadius > 0, "x-radius must be > 0")
    require((!params.types.contains(Type.POLYGON)) || params.maxYRadius > 0, "y-radius must be > 0")
    require((!params.types.contains(Type.POLYGON)) || params.polyPoints > 0, "y-radius must be > 0")
    
    val geoms = params.types.iterator.flatMap { t => 
      t match {
        case Type.POINT => 
          new PointGenerator(params.minX, params.maxX, params.minY, params.maxY, params.num).iterator
        case Type.POLYGON =>
          new PolygonGenerator(params.minX, params.maxX, params.minY, params.maxY, params.maxXRadius, params.maxYRadius,params.polyPoints, params.num).iterator
      }
    }.map(_.wkt)
    
    val times = params.interval.map( i => new IntervalGenerator(i, params.num * params.types.size)).map(_.iterator.map(_.mkString(';')))
    
    
    val geoTimes = times.map(ts => geoms.zip(ts).map{case (g,t) => s"${g};${t}"}).getOrElse(geoms)
    
    if(params.id)
      geoTimes.zipWithIndex.map{ case (obj, id) => s"$id;$obj" }
    else
      geoTimes
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