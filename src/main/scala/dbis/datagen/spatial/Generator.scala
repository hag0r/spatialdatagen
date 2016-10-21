package dbis.datagen.spatial

object Generator {
  
  def main(args: Array[String]) {
    
    // TODO: add CLI options to configure parameters (type, min, max, num, etc...)
    val polygons = new PolygonGenerator(5,4,-180,180,10)
    polygons.map(_.wkt).foreach(println)
    
    println
    
    val points = new PointGenerator(-180, 180, 10)
    points.map(_.wkt).foreach(println)
    
  }
  
}