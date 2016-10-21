package dbis.datagen.spatial

import scala.util.Random
import dbis.datagen.spatial.types.Polygon
import org.scalacheck.Gen

class PolygonGenerator(maxRadius1: Double, maxRadius2: Double, approx: Int, min: Double, max: Double, n: Long) extends AbstractGenerator[Polygon] {
  
  private val pg = new PointGenerator(min, max, approx)
  
  
  override def iterator = for(i <- (0L until n).iterator) yield createPolygon 
  
  
  /*
   * TODO: 
   * create ellipse around a random center point, distribute points on the ring
   * (with a deviation) and create a polygon from these points 
   */
  def createPolygon: Polygon = {
    
    val center = pg.pointGen.sample.get
    
    
    
    new Polygon(pg.toSeq) 
  }
  
  
}