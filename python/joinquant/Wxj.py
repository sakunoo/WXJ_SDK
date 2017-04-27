# -*- coding: utf-8 -*-

import requests

"""
简易说明：
    调用前初始化：
    g.trader = Wxj(微型金ID,'微型金ID对应的授权码')

    初始化完毕后，调用如下：

    查询资金信息
    g.trader.query_funds();

    查询持仓
    g.trader.query_hold();

    委托下单
    g.trader.buy(股票代码,下单价格,下单数量);

    委托卖出
    g.trader.sell(股票代码,委托价格 ,委托数量)
    
    委托卖出（只保留指定数量）
    g.trader.sell_target(股票代码,委托数量):

    各个接口具体返回的数据以及格式请查阅API文档。https://www.wxjin.com/agreement/api
"""


class Wxj(object):
    host_url = "https://api.wxjin.com/trade/api/third/"
    # 微型金ID
    fund_id = None
    # 微型金ID对应的授权码
    authorize = None

    def __init__(self, fund_id, authorize):
        self.fund_id = fund_id
        self.authorize = authorize

    def ops_put(self, params):
        pass_info = {'fundId': self.fund_id, 'authorize': self.authorize}
        return dict(params, **pass_info)

    # 查询资金信息
    def query_funds(self):
        params = {}
        url = self.host_url + 'fundInfo'
        res = requests.post(url, data=self.ops_put(params)).json()
        print('[WXJ]-账户资金: {0}'.format(res))
        return res

    # 查询持仓
    def query_hold(self):
        params = {}
        url = self.host_url + 'holdsList'
        res = requests.post(url, data=self.ops_put(params)).json()
        print('[WXJ]-账户持仓: {0}'.format(res))
        return res

    # 委托下单
    # stock_code    : 股票代码
    # entrust_price : 委托价格
    # entrust_num   : 委托数量
    def buy(self, stock_code, entrust_price, entrust_num):
        try:
            params = {'stockCode': stock_code, 'entrustPrice': float(entrust_price), 'entrustAmount': int(entrust_num)}
            url = self.host_url + 'buy'
            print('[WXJ]-买入股票: {0}'.format(params))
            res = requests.post(url, data=self.ops_put(params)).json()
            print('[WXJ]-买入股票返回结果: {0}'.format(res))
        except Exception as e:
            print(e)

    # 委托卖出
    # stock_code    : 股票代码
    # price         : 委托价格
    # num           : 委托数量
    def sell(self, stock_code, price, num):
        try:
            params = {'stockCode': stock_code, 'entrustPrice': float(price), 'entrustAmount': num}
            url = self.host_url + 'sell'
            print('[WXJ]-卖出股票: {0},价格{1},{2}'.format(stock_code, price, params))
            res = requests.post(url, data=self.ops_put(params)).json()
            print('[WXJ]-卖出股票返回结果: {0}'.format(res))
        except Exception as e:
            print(e)
            
    # 卖出（只保留指定数量），用法同聚宽API sell_target 函数
    # stock_code    : 股票代码
    # price         : 委托价格
    # num           : 委托数量
    def sell_target(self,stock_code,num):
        try:
            holds = self.query_hold()
            price = 0
            sellNum = 0
            if holds != None and holds['data'] != None:
                for hold in holds['data']:
                    if stock_code == hold['stockCode']:
                        price = hold['lastPrice']
                        sellNum = int(hold['enableAmount']) - num
                return self.sell(stock_code,float(price),int(sellNum))
        except Exception as e:
            print (e)