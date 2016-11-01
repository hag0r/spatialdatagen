package dbis.datagen.spatial

import scala.util.Random
import dbis.setm.SETM._

object BasicGenerators {
  
  def double(min: Double, max: Double): Double = timing("random double") {
    require(max >= min, s"max ($max) must be greater than min ($min)")
    min + Random.nextDouble() * (max - min) 
  }

  
  def double(min: Double, max: Double, f: Double => Boolean): Double = timing("random double with condition") {
    var r = 0.0 
      
    do { 
      r = double(min, max) 
    } while(!f(r));
    
    return r
    
  } 
  
  def int(min: Int, max: Int): Int = timing("random int") {
    require(max >= min, s"max ($max) must be greater than min ($min)")
    
    min + Random.nextInt(max - min) 
    
  } 
  
  def int(min: Int, max: Int, f: Int => Boolean): Int = timing("random int with condition") {
    var r = 0
    do {
      r = int(min, max)
    } while(!f(r))
      
    return r
  } 
}