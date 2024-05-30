import FinanceDataReader as fdr
import mplfinance
from datetime import datetime
from dateutil.relativedelta import relativedelta
import pandas as pd
import matplotlib.pyplot as plt
import talib as ta
import requests
import sys
import os

if __name__ == "__main__":
#     sys.stdout.reconfigure(encoding='utf-8')
    arg = sys.argv[1:]
    file_check = datetime.today().strftime("%Y-%m-%d-%H")
    path = f'{os.getcwd()}/src/main/resources/static/graph/'
    dirpath = f'{path}/{arg[0]}'
    filepath = f'{dirpath}/{arg[0]}_bol.png'
    lock = True
    if not os.path.isdir(dirpath):
        os.mkdir(dirpath)
    else:
        if os.path.isfile(filepath):
            m_time = datetime.fromtimestamp(os.path.getmtime(filepath)).strftime("%Y-%m-%d-%H")
            m_arr = m_time.split("-")
            f_arr = file_check.split("-")
            for i in range(len(m_arr)):
                if int(m_arr[i]) != int(f_arr[i]):
                    lock = False
            if lock:
                exit()



present_day = datetime.today().strftime("%Y-%m-%d")
three_month_ago = (datetime.today()-relativedelta(months=3)).strftime("%Y-%m-%d")
year_ago = (datetime.today()-relativedelta(years=1)).strftime("%Y-%m-%d")
m_color = mplfinance.make_marketcolors(up='r', down='b', inherit=True)
style = mplfinance.make_mpf_style(marketcolors=m_color)

def get_daily_price():
    # 일봉
    daily_price = fdr.DataReader(arg[0], three_month_ago, present_day)
    daily_price_reverse = daily_price.iloc[::-1]
    daily_price_reverse = daily_price_reverse[:50]
    daily_price = daily_price_reverse.iloc[::-1]
    d_path = dirpath+'/'+arg[0]+'_day'+'.png'
    mplfinance.plot(daily_price, type='candle', style=style, figratio=(15, 8), volume=True, savefig=d_path)

def get_week_price():
    # 주봉
    week_json = requests.get(f'https://api.stock.naver.com/chart/domestic/item/{arg[0]}?periodType=weekCandle')
    week_price = pd.DataFrame(week_json.json().get('priceInfos'))
    week_price['localDate'] = pd.to_datetime(week_price['localDate'], format='%Y%m%d')
    week_price.columns = ['Date', 'Close', 'Open', 'High', 'Low', 'Volume', 'RetentionRate']
    week_price = week_price.set_index('Date')
    week_price_reverse = week_price.iloc[::-1]
    week_price_reverse = week_price_reverse[:50]
    week_price = week_price_reverse.iloc[::-1]
    w_path = dirpath+'/'+arg[0]+'_week'+'.png'
    mplfinance.plot(week_price, type='candle', style=style, figratio=(15, 8), volume=True, savefig=w_path)

def get_month_price():
    # 월봉
    month_json = requests.get(f'https://api.stock.naver.com/chart/domestic/item/{arg[0]}?periodType=monthCandle')
    month_price = pd.DataFrame(month_json.json().get('priceInfos'))
    month_price['localDate'] = pd.to_datetime(month_price['localDate'], format='%Y%m%d')
    month_price.columns = ['Date', 'Close', 'Open', 'High', 'Low', 'Volume', 'RetentionRate']
    month_price = month_price.set_index('Date')
    month_price_reverse = month_price.iloc[::-1]
    month_price_reverse = month_price_reverse[:50]
    month_price = month_price_reverse.iloc[::-1]
    m_path = dirpath+'/'+arg[0]+'_month'+'.png'
    mplfinance.plot(month_price, type='candle', style=style, figratio=(15, 8), volume=True, savefig=m_path)

def get_bol_rsi():
    #data 데이터프레임에 담긴 Code로 DataReader를 이용하여 주가정보를 가져온다.
    df = fdr.DataReader(arg[0], year_ago, present_day)
    df['Change'] = df['Change'] * 100
    #주가정보를 가져온후 Close값을 기준으로 이동평균선을 구하고 RSI를 계산한다.
    df['MA'] = ta.SMA(df['Close'], 7)
    #RSI를 계산한후 RSI_signal을 계산하여 이동평균선을 구한다.
    df['RSI'] = ta.RSI(df['Close'], 7)
    df["RSI_signal"] = df["RSI"].rolling(7).mean()

    # 볼린저밴드 계산 및 그래프 그리기
    df['BBAND_UPPER'], df['BBAND_MIDDLE'], df['BBAND_LOWER'] = ta.BBANDS(df['Close'], 20, 2)
    bollinger_up = mplfinance.make_addplot(df['BBAND_UPPER'], color='orange')
    bollinger_mid = mplfinance.make_addplot(df['BBAND_MIDDLE'], color='black', linestyle='dashed')
    bollinger_low = mplfinance.make_addplot(df['BBAND_LOWER'], color='green')

    m_color = mplfinance.make_marketcolors(up='r', down='b', inherit=True)
    style = mplfinance.make_mpf_style(marketcolors=m_color)
    bol_path = dirpath+'/'+arg[0]+'_bol'+'.png'
    c_b = mplfinance.plot(df, type='candle', addplot=[bollinger_up, bollinger_mid, bollinger_low], style=style, figratio=(15, 8), volume=True, savefig=bol_path)

    # 한 영역에 2행 1열로 나누어 그래프 그리기
    fig, axs = plt.subplots(2, 1, figsize=(10, 8), sharex=True)

    # 첫 번째 subplot에 주식 종가 그래프 그리기
    df["Close"].plot(ax=axs[0])
    axs[0].set_title('Stock Close Price')

    # 두 번째 subplot에 RSI와 RSI 신호 그래프 그리기
    df[["RSI", "RSI_signal"]].plot(ax=axs[1])
    axs[1].set_title('RSI and RSI Signal')
    axs[1].set_ylim(0, 100)
    axs[1].axhline(70, color='r', linestyle='--')
    axs[1].axhline(50, color='k', linestyle='--')
    axs[1].axhline(30, color='b', linestyle='--')
    rsi_path = dirpath+'/'+arg[0]+'_rsi'+'.png'
    plt.savefig(rsi_path)

get_daily_price()
get_week_price()
get_month_price()
get_bol_rsi()