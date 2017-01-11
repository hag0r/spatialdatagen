package dbis.datagen

import java.nio.file.Path
import dbis.datagen.Generator.Type
import scopt.OptionParser
import java.io.File
import dbis.datagen.st.types.Interval

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
  minX: Double = -180,
  maxX: Double = 180,
  minY: Double = -90,
  maxY: Double = 90,
  maxXRadius: Double = Double.NaN,
  maxYRadius: Double = Double.NaN,
  num: Int = Generator.NUM_DEFAULT,
  polyPoints: Int = Generator.POLYPOINTS_DEFAULT,
  id: Boolean = false,
  quiet: Boolean = false,
  file: Option[Path] = None,
  types: Array[Type.Type] = Array.empty,
  interval: Option[Interval] = None
) 

object Params {
  
  def parseArgs(args: Array[String]): Params = {
    val parser = new OptionParser[Params]("Spatial Data Generator") {
      head("Spatial Data Generator", s"ver. 0.1")
      help("help") text ("prints this usage text")
      opt[Double]("min-x") optional() action { (x,c) => c.copy(minX = x) } text("Minimal x value (for polygons for the center)")
      opt[Double]("max-x") optional() action { (x,c) => c.copy(maxX = x) } text("Maximal x value (for polygons for the center)")
      opt[Double]("min-y") optional() action { (x,c) => c.copy(minY = x) } text("Minimal y value (for polygons for the center)")
      opt[Double]("max-y") optional() action { (x,c) => c.copy(maxY = x) } text("Maximal y value (for polygons for the center)")
      opt[Double]("x-radius") optional() action { (x,c) => c.copy(maxXRadius = x) } validate( x => if (x > 0) success else failure("Value <x-radius> must be >0") ) text("Maximal x radius of the ellipse to generate polygons (only needed for polygons)")
      opt[Double]("y-radius") optional() action { (x,c) => c.copy(maxYRadius = x) } validate( x => if (x > 0) success else failure("Value <y-radius> must be >0") ) text("Maximal y radius of the ellipse to generate polygons (only needed for polygons)")
      opt[Int]('a',"approx") optional() action { (x,c) => c.copy(polyPoints = x) } validate( x => if (x >= 3) success else failure("Value <approx> must be >=3") ) text(s"Maximun number of points to use to represent a polygon, default = ${Generator.POLYPOINTS_DEFAULT}")
      opt[Int]('n',"num") optional() action { (x,c) => c.copy(num = x) } validate( x => if (x > 0) success else failure("Value <num> must be >0") ) text(s"Number of elements to generate in total, default = ${Generator.NUM_DEFAULT}")
      opt[Unit]("id") optional() action { (_,c) => c.copy(id = true) } text("Generate an ID (Long) for each object")
      opt[Unit]('q', "quiet") optional() action { (_,c) => c.copy(quiet = true) } text("Do not print execution time statistics")
      opt[Seq[String]]('t',"types") required() action { (x,c) => 
        c.copy(types = x.map(t => Type.withName(t.toUpperCase())).toArray)} text(s"Comma separated list of types to generate (${Type.values.map(_.toString().toLowerCase()).mkString(",")})")
      opt[Seq[Long]]('i',"interval") optional() action { (x,c) => c.copy(interval = Some(Interval(x(0),x(1)))) } validate (x => if(x.size == 2 && x(0) < x(1)) success else failure("Expected exactly two comma separated values")) text ("create interval with given min and max bounds")
      arg[File]("file") optional() action { (x, c) => c.copy(file = Some(x.toPath())) } text ("Output file to write results to. Use <stdout> if empty")
      
    }
    
      parser.parse(args, Params()).getOrElse {
        System.exit(1)
        Params() // should never get here - it's just to have the correct return type
      }
  }
  
}