package dbis.datagen

import scala.util.Random
import dbis.setm.SETM._
import java.util.concurrent.ThreadLocalRandom

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
  
  def long(min: Long, max: Long): Long = timing("random Long") {
    require(max >= min, s"max ($max) must be greater than min ($min)")
    
    ThreadLocalRandom.current().nextLong(min, max)
  } 
  
  def Long(min: Long, max: Long, f: Long => Boolean): Long = timing("random Long with condition") {
    var r = 0L
    do {
      r = long(min, max)
    } while(!f(r))
      
    return r
  } 
}