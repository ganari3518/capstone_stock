import pandas as pd
import FinanceDataReader as fdr
from datetime import datetime
from dateutil.relativedelta import relativedelta
import os
import sys

sys.stdout.reconfigure(encoding='utf-8')

present_day = datetime.today().strftime("%Y-%m-%d")
month_ago = (datetime.today() - relativedelta(months=1)).strftime("%Y-%m-%d")

#달러/유로, 달러/엔화, 달러/원, 달러/위안, 원화/엔화
usd_eur = fdr.DataReader("USD/EUR", month_ago, present_day).iloc[-1:]
usd_jpy = fdr.DataReader("USD/JPY", month_ago, present_day).iloc[-1:]
usd_krw = fdr.DataReader("USD/KRW", month_ago, present_day).iloc[-1:]
usd_cny = fdr.DataReader("USD/CNY", month_ago, present_day).iloc[-1:]
krw_jpy = fdr.DataReader("KRW/JPY", month_ago, present_day).iloc[-1:]

result = pd.concat([usd_eur, usd_jpy, usd_krw, usd_cny, krw_jpy])
result['Name'] = ['USD/EUR', 'USD/JPY', 'USD/KRW', 'USD/CNY', 'KRW/JPY']
print(result.to_json(orient='records', force_ascii=False))