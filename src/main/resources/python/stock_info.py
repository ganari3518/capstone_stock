import FinanceDataReader as fdr
from datetime import datetime
from dateutil.relativedelta import relativedelta
import sys

if __name__ == "__main__":
    sys.stdout.reconfigure(encoding='utf-8')
    arg = sys.argv[1:]

present_day = datetime.today().strftime("%Y-%m-%d")
month_ago = (datetime.today() - relativedelta(months=1)).strftime("%Y-%m-%d")

stock_info = fdr.DataReader(arg[0], month_ago, present_day)
stock_info = stock_info.iloc[-1:]

print(stock_info.to_json(orient='records', force_ascii=False))