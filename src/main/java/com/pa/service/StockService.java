package com.pa.service;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.OHLC;
import com.zerodhatech.models.OHLCQuote;

import java.io.IOException;
import java.util.Map;

public interface StockService  {


  void placeAmoOrder(int qty, String orderType, String tradingSymbol, String exchange,
                     String transactionType, Double price) throws KiteException, IOException;

  //Map<String, OHLCQuote> getOHLC(String[] instruments) throws KiteException, IOException;


  void placeAmoOrder();


  void placeAmoSellOrder();

}
