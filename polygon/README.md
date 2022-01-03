# Polygon Network

<br>

## Uniswap3 Pool

Uniswap Pool Range tracker

### Prerequisites

1. Cast ([https://github.com/gakonst/foundry](https://github.com/gakonst/foundry))
2. Ammonite ([https://github.com/com-lihaoyi/Ammonite](https://github.com/com-lihaoyi/Ammonite))

Utils must be installed and available in $PATH

### Checking Pool Range

Checks Token Pair pool for specific address

```
./uni3-nft.sh <EOA-address> <Pool-address>
```

Example:
```
./uni3-nft.sh $ETH_FROM 0x86f1d8390222a3691c28938ec7404a1661e618e0

pool: 0x86f1d8390222a3691c28938ec7404a1661e618e0: tick = -73332
pool: 6022: liquidity = 0
pool: 6281: liquidity = 0
pool: 7970: liquidity = 944247437131056871204 (944)
range: [-73810,-72010]: IN

pool: 8170: liquidity = 0
pool: 9307: liquidity = 0
pool: 10800: liquidity = 0
pool: 10874: liquidity = 0
pool: 11917: liquidity = 0
pool: 12469: liquidity = 6266985094118314956287 (6266)
range: [-73580,-73160]: IN
```




 
