package dbis.datagen.spatial

import scala.util.Random
import dbis.datagen.spatial.types.Polygon
import dbis.datagen.spatial.types.Point

/**
 * A generator class for polygons
 */
class PolygonGenerator(
    minX: Double, maxX: Double, minY: Double, maxY: Double, 
    maxRadius1: Double, maxRadius2: Double, 
    approx: Int, n: Long) extends AbstractGenerator[Polygon] {
  
  require(approx >= 3, "approximation points must be at least 3")
  
  private val pg = new PointGenerator(minX, maxX, minY, maxY, approx)
  
  
  override def iterator = for(i <- (0L until n).iterator) yield createPolygon 
  
  
  def createPolygon: Polygon = {
    
    // center point for ellipse
    val center = PointGenerator.point(minX, maxX, minY, maxY)
    
    // create radius for ellipse
		val xRadius = BasicGenerators.double(1, maxRadius1)
		val yRadius = BasicGenerators.double(1, maxRadius2)
    
		// we want at least 3 points
		def numPoints = BasicGenerators.int(3, approx)
		
		// first create numPoints angles
    var prevAngle = 0.0
    val points = (0 until numPoints).map { i => 
      val a = BasicGenerators.double(prevAngle, 359) 
      prevAngle = a
      a
    }
    .map { a => math.toRadians(a)} // cos and sin expect Rad !!!
    .map { angle =>  // convert each angle into a point on the ellipse
      val x = xRadius * math.cos(angle) + center.x 
      val y = yRadius * math.sin(angle) + center.y
      
      Point(x,y)
    }.toList // collect points
    
    new Polygon(points :+ points.head) // append starting point 
  }
  
  
}