import requests
import re
import FinanceDataReader as fdr
import time
from datetime import datetime, timedelta
from yahoo_fin.stock_info import get_stats_valuation
from tqdm import tqdm
import talib as ta
from bs4 import BeautifulSoup
import pandas as pd
import pymysql as mysql

discordKeys = []
telegramKeys = []

conn = mysql.connect(host="localhost", user="root", password="1111", db="stock", use_unicode=True, charset="utf8")
cur = conn.cursor()

cur.execute("SELECT apikey from tb_post where delete_yn = 0 and service = '디스코드'")

res = cur.fetchall()

for row in res:
    discordKeys.append(row[0])

cur.execute("SELECT apikey from tb_post where delete_yn = 0 and service = '텔레그램'")

res = cur.fetchall()

for row in res:
    telegramKeys.append(row[0])

# print(discordKeys)
# print(telegramKeys)

cur.close()
conn.close()

## 비트코인과 환율 정보를 받아온다
## 스팩 낮은 가격순으로 5개 가져온다.

class Scrap:
    start_time = time.time()

    def send_message(messages):
        for key in discordKeys:
            message = {"content": f"{str(messages)}"}
            requests.post(key, data=message)

        for key in telegramKeys:
            bot_token, chat_id = key.split("/")
            data = {"chat_id": chat_id, "text": messages}
            send_url = f"https://api.telegram.org/bot{bot_token}/sendmessage"
            requests.get(send_url, data=data)

    def get_diff_and_per(par):
        diffs = round(par.Close.iloc[-2] - par.Close.iloc[-1], 2)
        diffs_per = round(((par.Close.iloc[-2] - par.Close.iloc[-1]) / par.Close.iloc[-1] ) * 100, 2)
        return diffs*-1, diffs_per*-1

    def get_stock_data(stock_code):
        return fdr.DataReader(stock_code)

    nasdaq = fdr.DataReader('IXIC')
    dollar = fdr.DataReader('USD/KRW')
    bitcoin = fdr.DataReader('BTC/KRW')
    now = datetime.now()
    send_message(f"****************각종 지수 및 스펙 낮은 가격순 5개 종목****************")
    send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] 나스닥 지수 최근 종가: {round(nasdaq.Close.iloc[-1])}")
    send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] 나스닥 전일대비 및 퍼센트: {get_diff_and_per(nasdaq)}")
    send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] 환율지수 최근 종가: {round(dollar.Close.iloc[-1])}")
    send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] 달러 전일대비 및 퍼센트: {get_diff_and_per(dollar)}")
    send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] 비트코인 최근 종가: {round(bitcoin.Close.iloc[-1])}")
    send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] 비트코인 전일대비 및 퍼센트: {get_diff_and_per(bitcoin)}")

    all_stocks = fdr.StockListing('KRX')
    spac_stocks = all_stocks[all_stocks['Name'].str.endswith('스팩')]
    spac_data = {}

    for idx, row in spac_stocks.iterrows():
        stock_code = row['Code']
        stock_data = get_stock_data(stock_code)
        spac_data[row['Name']] = {
            'Price': stock_data['Close'].values[-1],
            'Listing Date': stock_data.index[0].strftime('%Y-%m-%d')
        }

    sorted_prices = sorted(spac_data.items(), key=lambda x: x[1]['Price'])

    for name, data in sorted_prices[:5]:
        send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] {name}: Price - {data['Price']}, Listing Date - {data['Listing Date']}")

    print("총 걸린 시간:", time.time() - start_time)

class StockBreakout:
    def __init__(self):
        self.kosdaq_stocks = fdr.StockListing('KOSDAQ')
        self.breakout_stocks = []
        self.no_data = []

    def send_message(self, messages):
        for key in discordKeys:
            message = {"content": f"{str(messages)}"}
            requests.post(key, data=message)

        for key in telegramKeys:
            bot_token, chat_id = key.split("/")
            data = {"chat_id": chat_id, "text": messages}
            send_url = f"https://api.telegram.org/bot{bot_token}/sendmessage"
            requests.get(send_url, data=data)

### 신고가 52주 돌파 종목
    def find_breakout_stocks(self):
        for idx, row in tqdm(self.kosdaq_stocks.iterrows(), total=len(self.kosdaq_stocks), desc='Progress'):
            stock_code = row['Code']
            stock_data = fdr.DataReader(stock_code)
            high_52week = stock_data['Close'].rolling(window=252, min_periods=1).max()

            # if stock_data['Close'].iloc[-1] > high_52week.iloc[-2]:
            #     self.breakout_stocks.append((row['Name'], stock_code))

            # 충분한 데이터 포인트가 있는지 확인
            if len(high_52week) >= 2:
                if stock_data['Close'].iloc[-1] > high_52week.iloc[-2]:
                    self.breakout_stocks.append((row['Name'], stock_code))
            else:
                self.no_data.append(f"{row['Name']} ({stock_code})에 대한 충분한 데이터가 없습니다.")

### 돌파종목 출력하기
    def print_breakout_stocks(self):
        now = datetime.now()
        self.send_message(f"****************신고가 52주 돌파 종목****************")
        for name, code in self.breakout_stocks:
            self.send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] {name} ({code})")
        for msg in self.no_data:
            self.send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] {msg}")

class StockAnalyzer:
    def __init__(self):
        self.today = datetime.today()
        self.two_years_ago = self.today - timedelta(days=365 * 2)
        self.recent_thursday = self.today - timedelta(days=(self.today.weekday() - 3) % 7)
        self.two_years_ago_second_thursday = self.two_years_ago + timedelta(
            days=(3 - self.two_years_ago.weekday() + 7) % 7 + 7)

    def get_kospi_stocks(self):
        df_krx = fdr.StockListing("KRX")
        return df_krx[df_krx['Market'] == 'KOSPI']

    def get_stock_name(stock_code):
        try:
            stock_listing = fdr.StockListing('KRX')
            stock_name = stock_listing[stock_listing['Code'] == stock_code]['Name'].values[0]
            return stock_name
        except IndexError:
            return "해당하는 종목 코드를 찾을 수 없습니다."

    def filter_selected_stocks(self, df):
        selected_stocks = []
        for symbol in df['Code']:
            stock_data = fdr.DataReader(symbol, start=self.two_years_ago_second_thursday, end=self.recent_thursday)
            if len(stock_data) >= 20:
                stock_data['SMA'] = stock_data['Close'].rolling(window=20).mean()
                last_sma = stock_data['SMA'].iloc[-1]
                if stock_data['Close'].iloc[-1] < (last_sma * 0.9):
                    selected_stocks.append(symbol)
        return selected_stocks

    def get_per(self, symbol):
        try:
            stats = get_stats_valuation(symbol)
            pe_ratio = stats.iloc[2, 1]
            return pe_ratio
        except Exception as e:
            print(f"Error fetching PER for {symbol}: {e}")
            return None

    def get_per_for_selected_stocks(self, symbols):
        per_values = []
        for symbol in tqdm(symbols, desc="Fetching PER data"):
            per = self.get_per(symbol)
            per_values.append(per)
        return per_values

    def send_message(self, msg):
        for key in discordKeys:
            message = {"content": f"{str(msg)}"}
            requests.post(key, data=message)

        for key in telegramKeys:
            bot_token, chat_id = key.split("/")
            data = {"chat_id": chat_id, "text": msg}
            send_url = f"https://api.telegram.org/bot{bot_token}/sendmessage"
            requests.get(send_url, data=data)
        print(message)

class ListKS:
    ##코스닥 종목에서 RSI 30미만 종목 추출하는 것입니다.
    # 현재 날짜 계산
    end_date = datetime.now()
    # 2년 전 날짜 계산
    start_date = end_date - timedelta(days=365 * 2)
    kosdaq = fdr.StockListing('KOSDAQ')
    # 선택된 종목을 저장할 리스트
    selected_stocks = []

    ## 주식코드 가져오기
    def get_stock_name(stock_code):
        try:
            stock_listing = fdr.StockListing('KRX')
            stock_name = stock_listing[stock_listing['Code'] == stock_code]['Name'].values[0]
            return stock_name
        except IndexError:
            return "해당하는 종목 코드를 찾을 수 없습니다."

    for symbol in kosdaq['Code']:
        # 주식 데이터 가져오기
        stock_data = fdr.DataReader(symbol, start_date, end_date)

        if len(stock_data) >= 20:  # 최소 20일 데이터가 있는지 확인
            stock_data['SMA'] = ta.SMA(stock_data['Close'], 14)
            stock_data['Lower'] = stock_data['SMA'] - (2 * stock_data['Close'].rolling(window=14).std())
            stock_data['RSI'] = ta.RSI(stock_data['Close'],14)
            if (stock_data['Close'].iloc[-1] < stock_data['Lower'].iloc[-1] and
                    stock_data['RSI'].iloc[-1] <= 30):
                    selected_stocks.append(symbol)
                    print(symbol, get_stock_name(symbol))

    def send_message(messages):
        for key in discordKeys:
            message = {"content": f"{str(messages)}"}
            requests.post(key, data=message)

        for key in telegramKeys:
            bot_token, chat_id = key.split("/")
            data = {"chat_id": chat_id, "text": messages}
            send_url = f"https://api.telegram.org/bot{bot_token}/sendmessage"
            requests.get(send_url, data=data)

    # 선택된 종목 출력
    # selected_stocks_data = kosdaq[kosdaq['Code'].isin(selected_stocks)].head(5)
    selected_stocks_data = kosdaq[kosdaq['Code'].isin(selected_stocks)][['Code','Name']]
    send_message("****************RSI 30미만 종목 추출****************")
    now = datetime.now()
    for idx, row in selected_stocks_data.iterrows():
        send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] 코드 : {row['Code']}, 이름 : {row['Name']}")

class NewList:
    def send_message(self, message):
        for key in discordKeys:
            msg = {"content": f"{str(message)}"}
            requests.post(key, data=msg)

        for key in telegramKeys:
            bot_token, chat_id = key.split("/")
            data = {"chat_id": chat_id, "text": message}
            send_url = f"https://api.telegram.org/bot{bot_token}/sendmessage"
            requests.get(send_url, data=data)

    # 네이버 새로 상장된 기업 가져오기
    def get_newly_listed_stocks(self):
        url = "https://finance.naver.com/sise/sise_new_stock.nhn?sosok=1"
        res = requests.get(url)
        soup = BeautifulSoup(res.text, 'html.parser')
        stocks = soup.select(".box_type_l a.tltle")
        stock_codes = [re.search(r'(\d+)', stock.get("href")).group(1) for stock in stocks]
        return stock_codes


    # 주식 데이터 가져오기
    def get_stock_data(self,stock_code):
        try:
            data = fdr.DataReader(stock_code)
            if len(data) >= 10:
                return data
            else:
                return None
        except Exception as e:
            print(f"Error occurred while fetching data for {stock_code}: {e}")
            return None

    # 주식 분석 및 결과 전송
    def analyze_stocks(self):
        stock_codes = self.get_newly_listed_stocks()
        now = datetime.now()
        self.send_message(f"****************새로 상장된 기업****************")
        for stock_code in stock_codes:
            if stock_code in krx_data["종목코드"].values:
                company_name = krx_data.loc[krx_data['종목코드'] == stock_code, '회사명'].values[0]
                data = self.get_stock_data(stock_code)
                if data is not None:
                    self.send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] 종목 코드: {stock_code}, 종목명: {company_name}")
            else:
                self.send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] 해당 주식 코드({stock_code})에 대한 정보를 찾을 수 없습니다.")


def main():
    stock_analyzer = StockAnalyzer()

    # 코스피 종목 데이터 가져오기
    kospi_stocks = stock_analyzer.get_kospi_stocks()

    # 시가총액 500억 이상인 종목 필터링
    selected_stocks = stock_analyzer.filter_selected_stocks(kospi_stocks)

    # PER 값 가져오기
    selected_symbols = kospi_stocks[kospi_stocks['Code'].isin(selected_stocks)]
    symbols = selected_symbols['Code'] + '.KS'
    names = selected_symbols['Name']

    # Fetching PER 데이터 프로그레스 바 추가
    per_values = stock_analyzer.get_per_for_selected_stocks(symbols)

    # 결과 출력
    stock_analyzer.send_message("****************코스피에서 시총500이상이며 불린저밴드평균 미만종목****************")
    now = datetime.now()
    for symbol, name, per in zip(symbols, names, per_values):
        stock_analyzer.send_message(f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] {symbol}: PER = {per} 종목명 : {name}")

if __name__ == "__main__":
    Scrap()

    stock_breakout = StockBreakout()
    stock_breakout.find_breakout_stocks()
    stock_breakout.print_breakout_stocks()

    main()

    ListKS()

    krx_data = \
    pd.read_html('http://kind.krx.co.kr/corpgeneral/corpList.do?method=download', header=0, encoding='euc-kr')[0]
    krx_data['종목코드'] = krx_data['종목코드'].map('{:06d}'.format)

    NewList().analyze_stocks()
