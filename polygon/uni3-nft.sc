import os._
import ujson._

case class Position(nonce:Long,operator:String,token0:String,token1:String,fee:Long,tickLower:Long,tickUpper:Long,liquidity:BigInt,feeGrowthInside0LastX128:BigInt,feeGrowthInside1LastX128:BigInt,tokensOwed0:BigInt,tokensOwed1:BigInt)

@main
def main(addr:String,pool:String="0x86f1d8390222a3691c28938ec7404a1661e618e0") {

  val CAST = "cast"
  val NET = Map("ETH_RPC_URL"->Option(System.getenv("ETH_RPC_URL")).getOrElse("https://polygon-mainnet.infura.io/v3/b828e5fc6a644ca39ccf99775b40977f"))
  val NFT_CONTRACT="0xC36442b4a4522E871399CD717aBDD847Ab11FE88"
  val TMP_EXPIRED="/tmp/polygon-pool-expired.tmp"

  var expired:Vector[Long] = {
    if(os.exists(os.Path(TMP_EXPIRED))) {
      val expired = os.read(os.Path(TMP_EXPIRED))
      expired.split("\n").map(_.trim).filter(!_.isEmpty).map(_.toLong).sorted.toVector
    } else
      Vector()
  }

  def is_expired(id:Long) = expired.indexOf(id) != -1

  Console.err.println(s"expired pools: ${expired}")

  val pool_info = os.proc("bash","uni3-polygon-pool.sh",pool).spawn(env=NET).stdout.text.trim
  println(pool_info)

  val r = ujson.read(pool_info)
  val tick = r.obj("data").obj("pool")("tick").str.toLong  

  Console.println(s"pool: ${pool}: tick = ${tick}")

  val positions = os.proc(CAST,"call",NFT_CONTRACT,"balanceOf(address)(uint256)",addr).spawn(env=NET).stdout.text.trim.toInt
  for( i <- 0 to positions - 1) {
    val pool_id = os.proc(CAST,"call",NFT_CONTRACT,"tokenOfOwnerByIndex(address,uint256)(uint256)",addr,i).spawn(env=NET).stdout.text.trim.toLong

    if(! is_expired(pool_id)) {

      // check the ownership 
      val owner = os.proc(CAST,"call",NFT_CONTRACT,"ownerOf(uint256)(address)",pool_id).spawn(env=NET).stdout.text.trim
      
      if(owner.toLowerCase != addr.toLowerCase) {
        Console.err.println(s"pool: ${pool_id}: new owner (${owner})")
        expired = (expired :+ pool_id).sorted
      } else {
      
        val data = os.proc(CAST,"call",NFT_CONTRACT,"positions(uint256)(uint96,address,address,address,uint24,int24,int24,uint128,uint256,uint256,uint128,uint128)",pool_id).spawn(env=NET).stdout.text.trim

        val p = data.split("\n") match { 
          case Array(nonce,operator,token0,token1,fee,tickLower,tickUpper,liquidity,feeGrowthInside0LastX128,feeGrowthInside1LastX128,tokensOwed0,tokensOwed1) => 
            Position(nonce.toLong,operator,token0,token1,fee.toLong,BigInt(tickLower).toLong,BigInt(tickUpper).toLong,BigInt(liquidity),BigInt(feeGrowthInside0LastX128),BigInt(feeGrowthInside1LastX128),BigInt(tokensOwed0),BigInt(tokensOwed1)) 
        }

        if(p.liquidity == 0) {
          Console.err.println(s"pool: ${pool_id}: liquidity = 0")
          expired = (expired :+ pool_id).sorted
        } else {
          val inRange = tick >= p.tickLower && tick <= p.tickUpper
          Console.println(
  s"""pool: ${pool_id}: liquidity = ${p.liquidity} (${p.liquidity / BigInt(1e18.toLong)})
  range: [${p.tickLower},${p.tickUpper}]: ${if(inRange) Console.GREEN+"IN"+Console.RESET else Console.RED+"OUT"+Console.RESET}
  """)
        }
      }
    }
  }

  os.write.over(os.Path(TMP_EXPIRED),expired.mkString("\n"))
  
}