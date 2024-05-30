import FinanceDataReader as fdr
import pandas as pd
import sys

sys.stdout.reconfigure(encoding='utf-8')
krx = fdr.StockListing("KRX")
krx = krx[['Name','Code']]
print(krx.to_json(orient='records', force_ascii=False))