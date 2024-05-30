import FinanceDataReader as fdr
import os
import sys

if __name__ == "__main__":
    sys.stdout.reconfigure(encoding='utf-8')
    arg = sys.argv[1:]

# KRX 종목 코드 불러오기
krx = fdr.StockListing('KRX')
# req = dict()

# 시가총액 상위 10개 종목 가져오기
top_10_market_cap = krx.nlargest(10, 'Marcap')
top10_json = top_10_market_cap.to_json(orient='records', force_ascii=False)
print(top10_json)