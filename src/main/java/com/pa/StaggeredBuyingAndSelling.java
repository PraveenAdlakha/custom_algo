package com.pa;

import com.pa.service.StockService;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.OHLCQuote;

import java.io.IOException;
import java.util.Map;

public class StaggeredBuyingAndSelling {


  StockService stockService;

  public StaggeredBuyingAndSelling(){
    stockService = new StockServiceImpl();
  }


//  public void placeStaggeredAmoBuyOrderOnCamarillaSupport(String[] symbols, int qty) throws KiteException, IOException {
//    String[] instruments = {"NSE:BAJFINANCE"};
//    System.out.println("in place order");
//    Map<String, OHLCQuote> ohlcMap =  stockService.getOHLC(symbols);
//
//    for (Map.Entry<String, OHLCQuote> entry : ohlcMap.entrySet())
//    {
//      OHLCQuote ohlc =  entry.getValue() ;
//      System.out.println("VWAP:" + (ohlc.ohlc.open + ohlc.ohlc.close)/2);
//      System.out.println(entry.getKey() + "/" + entry.getValue());
//    }
//  }
}
