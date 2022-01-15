import os._
import ujson._
import scala.util.control.Breaks._

type uint = Double

case class Tick(p0:uint,p1:uint,tick:Long)

@main
def main(json:String, currentTick:Long = -72591) {

  def getTicks(json:String):Seq[Tick] = {
    val liqJson = os.read(os.Path(json,os.pwd))
    val r = ujson.read(liqJson)
    val ticks = r.obj("data").obj("ticks").arr
    val tt = ticks.map( t => Tick(t.obj("price0").str.toDouble,t.obj("price1").str.toDouble,t.obj("tickIdx").str.toLong))
    tt.toIndexedSeq
  }

  val ticks = getTicks(json)

  var t1 = ticks(0)
  var t2 = t1
  breakable { for( i <- 1 to ticks.size) {
    val t = ticks(i)
    if(t.tick < currentTick) t1 = t
    if(t.tick > currentTick) { 
      t2 = t;
      break
    }
  }}
  //val currentRange = ticks.grouped(2).toSeq.collect{ case List(t1,t2) => (t1,t2)}.filter{ case (t1,t2) => t1.tick >= currentTick && t2.tick <= currentTick }

  println(s"${currentTick}: [${t1},${t2}]: price: [${t1.p1},${t2.p1}]")


  // val ii = data.iterator
  // val nonce = ii.take(1).mkString.toLong
  // val operator = ii.take(40+2).mkString
  // val token0 = ii.take(40+2).mkString
  // val token1 = ii.take(40+2).mkString
  // val fee = ii.take(24 / 8).mkString.toLong
  // val tickLower = ii.take(24 / 8).mkString.toLong
  // val tickUpper = ii.take(24 / 8).mkString.toLong
}
