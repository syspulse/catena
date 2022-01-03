POOL=${1:-0x86f1d8390222a3691c28938ec7404a1661e618e0}

curl 'https://api.thegraph.com/subgraphs/name/ianlapham/uniswap-v3-polygon' \
  -H 'authority: api.thegraph.com' \
  -H 'accept: */*' \
  -H 'content-type: application/json' \
  -H 'user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36' \
  -H 'sec-ch-ua-platform: "Linux"' \
  -H 'origin: https://info.uniswap.org' \
  -H 'sec-fetch-site: cross-site' \
  -H 'sec-fetch-mode: cors' \
  -H 'sec-fetch-dest: empty' \
  -H 'referer: https://info.uniswap.org/' \
  -H 'accept-language: en-US,en;q=0.9,de-DE;q=0.8,de;q=0.7,ja-JP;q=0.6,ja;q=0.5' \
  --data-raw "{\"operationName\":\"pool\",\"variables\":{\"poolAddress\":\"$POOL\"},\"query\":\"query pool(\$poolAddress: String\u0021) {\\n  pool(id: \$poolAddress) {\\n    tick\\n    token0 {\\n      symbol\\n      id\\n      decimals\\n      __typename\\n    }\\n    token1 {\\n      symbol\\n      id\\n      decimals\\n      __typename\\n    }\\n    feeTier\\n    sqrtPrice\\n    liquidity\\n    __typename\\n  }\\n}\\n\"}" \
  --compressed
