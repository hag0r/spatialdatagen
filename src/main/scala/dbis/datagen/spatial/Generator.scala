package dbis.datagen.spatial

import scala.collection.JavaConverters._
import scopt.OptionParser
import java.nio.file.Path
import java.nio.file.Paths
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption

/**
 * A generator for spatial data. It can create a set of points and polygons with 
 * specified characteristics. 
 */
object Generator {
  
  val NUM_DEFAULT = 10
  val POLYPOINTS_DEFAULT = 100
  
  object GeomType extends Enumeration {
    type GeomType = Value
    val POINT, POLYGON= Value
  }
  
  
  /**
   * Parameters for the generator
   * 
   * @param minX minimal X value to generate (e.g., for real world coordinates)
   * @param maxX maximal X value to generate (e.g., for real world coordinates)
   * @param maxXRadius maximal radius in X for the ellipse used for Polygon generation
   * @param maxYRadius maximal radius in Y for the ellipse used for Polygon generation
   * @param num Number of elements to create for each type
   * @param polyPoints Maximum number of points to use to describe a polygon
   * @param file An optional file name to write generated data to, leave empty to write to stdout
   * @param types A list of spatial data types to create 
   */
  case class Params(
    minX: Double = Double.NaN,
    maxX: Double = Double.NaN,
    minY: Double = Double.NaN,
    maxY: Double = Double.NaN,
    maxXRadius: Double = Double.NaN,
    maxYRadius: Double = Double.NaN,
    num: Int = NUM_DEFAULT,
    polyPoints: Int = POLYPOINTS_DEFAULT,
    file: Option[Path] = None,
    types: Array[GeomType.GeomType] = Array.empty
  ) 
  
  def main(args: Array[String]) {
    
    val parser = new OptionParser[Params]("Spatial Data Generator") {
      head("Spatial Data Generator", s"ver. 0.1")
      help("help") text ("prints this usage text")
      opt[Double]("min-x") required() action { (x,c) => c.copy(minX = x) } text("Minimal x value (for polygons for the center)")
      opt[Double]("max-x") required() action { (x,c) => c.copy(maxX = x) } text("Maximal x value (for polygons for the center)")
      opt[Double]("min-y") required() action { (x,c) => c.copy(minY = x) } text("Minimal y value (for polygons for the center)")
      opt[Double]("max-y") required() action { (x,c) => c.copy(maxY = x) } text("Maximal y value (for polygons for the center)")
      opt[Double]("x-radius") optional() action { (x,c) => c.copy(maxXRadius = x) } validate( x => if (x > 0) success else failure("Value <x-radius> must be >0") ) text("Maximal x radius of the ellipse to generate polygons (only needed for polygons)")
      opt[Double]("y-radius") optional() action { (x,c) => c.copy(maxYRadius = x) } validate( x => if (x > 0) success else failure("Value <y-radius> must be >0") ) text("Maximal y radius of the ellipse to generate polygons (only needed for polygons)")
      opt[Int]('a',"approx") optional() action { (x,c) => c.copy(polyPoints = x) } validate( x => if (x >= 3) success else failure("Value <approx> must be >=3") ) text(s"Maximun number of points to use to represent a polygon, default = $POLYPOINTS_DEFAULT")
      opt[Int]('n',"num") optional() action { (x,c) => c.copy(num = x) } validate( x => if (x > 0) success else failure("Value <num> must be >0") ) text(s"Number of elements to generate in total, default = $NUM_DEFAULT")
      opt[Seq[String]]('t',"types") required() action { (x,c) => 
        c.copy(types = x.map(t => GeomType.withName(t.toUpperCase())).toArray)} text(s"Comma separated list of types to generate (${GeomType.values.map(_.toString().toLowerCase()).mkString(",")})")
      arg[File]("file") optional() action { (x, c) => c.copy(file = Some(x.toPath())) } text ("Output file to write results to. Use <stdout> if empty")
      
    }
    
    try {
      parser.parse(args, Params()) match {
        case Some(p) => print(generate(p), p.file)
        case None => return // error will have been thrown 
      }
    } catch {
      case e: IllegalArgumentException => Console.err.println(s"[ERROR] Invalid input parameter: ${e.getMessage}")
    }
    
  }
  
  /**
   * Perform data generation using the characteristics specified in params
   * 
   * @param params The parameters for generation
   */
  def generate(params: Params): Iterator[String] = {
    
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
    
    params.types.iterator.flatMap { t => 
      t match {
        case GeomType.POINT => 
          new PointGenerator(params.minX, params.maxX, params.minY, params.maxY, params.num).iterator
        case GeomType.POLYGON =>
          new PolygonGenerator(params.minX, params.maxX, params.minY, params.maxY, params.maxXRadius, params.maxYRadius,params.polyPoints, params.num).iterator
      }
    }.map(_.wkt)
  }
  
  /**
   * Print elements to stdout or file 
   * 
   * @param elems The list of elements to print
   * @param file the file name to write to, if empty (None) use stdout 
   */
  def print(elems: Iterator[String], file: Option[Path]) {
    
    if(file.isDefined)
      Files.write(file.get, elems.toStream.asJava, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)
    else
      elems.foreach(println)
    
  }
  
}