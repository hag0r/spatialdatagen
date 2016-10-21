package dbis.datagen.spatial

import scala.collection.JavaConverters._

object Generator {
  
  def main(args: Array[String]) {
    
    // TODO: add CLI options to configure parameters (type, min, max, num, etc...)
    val polygons = new PolygonGenerator(100,90,1000,-180,180,100)
    java.nio.file.Files.write(java.nio.file.Paths.get("polies.txt"), polygons.map(_.wkt).asJava, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.WRITE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING)
    
    
    val points = new PointGenerator(-180, 180, 100)
    java.nio.file.Files.write(java.nio.file.Paths.get("points.txt"), points.map(_.wkt).asJava, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.WRITE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING)
    
  }
  
}