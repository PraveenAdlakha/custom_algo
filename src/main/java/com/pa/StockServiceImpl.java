package com.pa;

import com.pa.service.StockService;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.objects.NativeMath.round;

@Service
public class StockServiceImpl implements StockService {

  @Autowired
  private KiteConnect kiteConnect;


  public Map<String, OHLCQuote> getOHLC(String[] instruments) throws KiteException, IOException {

    try {


      //System.out.println(kiteConnect.getOHLC(instruments).get("256265").lastPrice);
      //System.out.println(kiteConnect.getOHLC(instruments).get("NSE:NIFTY 50").ohlc.open);
      // OHLC bajFinOhlc = kiteConnect.getOHLC(instruments).get("BSE:BAJFINANCE").ohlc;
      // System.out.println("OPEN: " + bajFinOhlc.open + " CLOSE: " + bajFinOhlc.close);


    } catch (Throwable e) {
      e.printStackTrace();
    }
    return kiteConnect.getOHLC(instruments);

  }


  /**
   * Place order.
   */
  public void placeAmoOrder(int qty, String orderType, String tradingSymbol, String exchange,
                            String transactionType, Double price) throws KiteException, IOException {
    /** Place order method requires a orderParams argument which contains,
     * tradingsymbol, exchange, transaction_type, order_type, quantity, product, price, trigger_price, disclosed_quantity, validity
     * squareoff_value, stoploss_value, trailing_stoploss
     * and variety (value can be regular, bo, co, amo)
     * place order will return order model which will have only orderId in the order model
     *
     * Following is an example param for LIMIT order,
     * if a call fails then KiteException will have error message in it
     * Success of this call implies only order has been placed successfully, not order execution. */
    //TO Pleace AMO order
    OrderParams orderParams = new OrderParams();
    orderParams.quantity = 1;
    orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
    orderParams.tradingsymbol = "BAJFINANCE";
    orderParams.product = Constants.PRODUCT_CNC;
    orderParams.exchange = Constants.EXCHANGE_NSE;
    orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
    orderParams.validity = Constants.VALIDITY_DAY;
    orderParams.price = 122.2;
    //orderParams.triggerPrice = 0.0;
    //orderParams.tag = "myTestBajFin"; //tag is optional and it cannot be more than 8 characters and only alphanumeric is allowed

    Order order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_AMO);
    System.out.println(order.orderId);
  }


  public void placeAmoOrder1() {


    OrderParams orderParams = new OrderParams();
    orderParams.quantity = 1;
    orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
    orderParams.tradingsymbol = "PERSISTENT";
    orderParams.product = Constants.PRODUCT_CNC;
    orderParams.exchange = Constants.EXCHANGE_NSE;
    orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
    orderParams.validity = Constants.VALIDITY_DAY;
    orderParams.price = 553.0;
    orderParams.triggerPrice = 0.0;
    orderParams.tag = "myTag";

    //TO Place regular order
//    OrderParams orderParams = new OrderParams();
//    orderParams.quantity = 1;
//    orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
//    orderParams.tradingsymbol = "PERSISTANT";
//    orderParams.product = Constants.PRODUCT_CNC;
//    orderParams.exchange = Constants.EXCHANGE_NSE;
//    orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
//    orderParams.validity = Constants.VALIDITY_DAY;
//    orderParams.triggerPrice = 0.0;
//    orderParams.price = 553.0;
    //orderParams.triggerPrice = 0.0;
    //orderParams.tag = "myTestBajFin"; //tag is optional and it cannot be more than 8 characters and only alphanumeric is allowed

    Order order = null;
    try {
      order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
    } catch (KiteException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(order.orderId);


//    String[] instruments = {"NSE:BAJFINANCE"};
//    System.out.println("in place order");
//    Map<String, OHLCQuote> ohlcMap = null;
//    try {
//      ohlcMap = getOHLC(instruments);
//    } catch (KiteException | IOException e) {
//      e.printStackTrace();
//    }
//
//    for (Map.Entry<String, OHLCQuote> entry : ohlcMap.entrySet()) {
//      OHLCQuote ohlc = entry.getValue();
//      System.out.println("VWAP:" + (ohlc.ohlc.open + ohlc.ohlc.close) / 2);
//      System.out.println(entry.getKey() + "/" + entry.getValue());
//    }
  }


  public void placeAmoOrder(){
    String[] instruments = {"NSE:BAJFINANCE"};
    Map<String, OHLCQuote> ohlcMap = null;
    try {
      Map<String, Quote> quotes = kiteConnect.getQuote(instruments);
      Quote quote = quotes.get("NSE:BAJFINANCE");
      Double todayLow = quote.ohlc.low;
      Double todayHigh = quote.ohlc.high;
      Double todayClose = quote.ohlc.close;
      Double pivot = (todayHigh + todayLow + todayClose) / 3;
      Double range = (todayHigh - todayLow);
      Double tomorrowBuyingPrice1 = pivot - range / 2;
      Double tomorrowBuyingPrice2 = pivot - range * 0.618;
      Double tomorrowBuyingPrice3 = pivot - range;
      Double tomorrowBuyingPrice4 = pivot - range * 1.382;
      OrderParams orderParams = new OrderParams();
      orderParams.quantity = 3;
      orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
      orderParams.tradingsymbol = "BAJFINANCE";
      orderParams.product = Constants.PRODUCT_CNC;
      orderParams.exchange = Constants.EXCHANGE_NSE;
      orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
      orderParams.validity = Constants.VALIDITY_DAY;
      orderParams.price = tomorrowBuyingPrice1;
      ArrayList<Double> list = new ArrayList<>();
      list.add(tomorrowBuyingPrice1);
      list.add(tomorrowBuyingPrice2);
      list.add(tomorrowBuyingPrice3);
      list.add(tomorrowBuyingPrice4);

      for(int i =0;i <3;i++){
        orderParams.price = round( list.get(i), 2);
        kiteConnect.placeOrder(orderParams, Constants.VARIETY_AMO);
      }
      Order order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_AMO);

      System.out.println(tomorrowBuyingPrice1 + " :: " + tomorrowBuyingPrice2 + "::" + tomorrowBuyingPrice3 + "::" +tomorrowBuyingPrice4) ;
    } catch (KiteException | IOException e) {
      e.printStackTrace();
    }

  }

  public void placeAmoSellOrder(){

    try {
      List<Holding> list = kiteConnect.getHoldings();
      for(Holding holding : list){
        System.out.println(holding.tradingSymbol + "::" + " quantity:" + holding.quantity + " price:" + holding.averagePrice);
        if(holding.tradingSymbol.equals("BAJFINANCE")){
          OrderParams orderParams = new OrderParams();
          orderParams.quantity = Integer.parseInt(holding.quantity);
          orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
          orderParams.tradingsymbol = "BAJFINANCE";
          orderParams.product = Constants.PRODUCT_CNC;
          orderParams.exchange = Constants.EXCHANGE_NSE;
          orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
          orderParams.validity = Constants.VALIDITY_DAY;
          orderParams.price = Double.valueOf( holding.averagePrice ) * 1.035;
          kiteConnect.placeOrder(orderParams, Constants.VARIETY_AMO);
        }

      }
    } catch (KiteException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
