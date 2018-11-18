package com.pa;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

public class BackTestingBajFinance {

  static double maxMoneySpent =0;
  static boolean isCapitalOver = false;
  static double maxCapital = 500000;

  public static void main(String[] args) {
    BackTestingBajFinance bajFinance = new BackTestingBajFinance();

    readDataLineByLine("/Users/padlakha/git/zerodha/mystock/src/main/resources/17-11-2016-TO-16-11-2018BAJFINANCEALLN.csv");
  }

  public static void readDataLineByLine(String file) {

    try {
      // Create an object of file reader
      // class with CSV file as a parameter.
      FileReader filereader = new FileReader(file);
      CSVReader csvReader = new CSVReaderBuilder(filereader)
          .withSkipLines(1)
          .build();
      List<String[]> allData = csvReader.readAll();
      Double tomorrowSellingPrice = 0.0;
      Double tomorrowBuyingPrice1;
      Double tomorrowBuyingPrice2;
      Double tomorrowBuyingPrice3;
      Double tomorrowBuyingPrice4;
      Double todayMoneySpent = 0.0;
      int quantityheld = 0;
      int quantityToBeBoughtPerOrder = 5;
      int quantityBoughtToday = 0;
      Double netProfit = 0.0;
      Double avgCostPrice = 0.0;
      Double totalMoneySpent = 0.0;

      for (int i = 0; i < allData.size(); i++) {
     // for (int i = 0; i < 442; i++) {
        todayMoneySpent = 0.0;
        String[] token = allData.get(i);
        String symbol = token[0];
        Double todayVwap = Double.valueOf(token[9]);
        Double todayLow = Double.valueOf(token[6]);
        Double todayOpen = Double.valueOf(token[4]);
        Double todayHigh = Double.valueOf(token[5]);
        String date = token[2];
        tomorrowBuyingPrice1 = todayVwap;
        tomorrowBuyingPrice2 = todayVwap * 0.99;
        tomorrowBuyingPrice3 = todayLow;
        tomorrowBuyingPrice4 = todayLow * 0.99;

        if (i == 0) {
          tomorrowSellingPrice = 0.0;
        } else {
          System.out.println("Buying prices: " + tomorrowBuyingPrice1 + "," + tomorrowBuyingPrice2 + "," + tomorrowBuyingPrice3 + "," + tomorrowBuyingPrice4 +
              " VWAP:" + todayVwap + " SellingPrice:" + tomorrowSellingPrice + " isInRange:" + inbetween(todayHigh, todayLow, tomorrowSellingPrice));
          System.out.println(token[0] + ",todayhigh:" + todayHigh + ",today Low:" + todayLow);

          if (inbetween(todayHigh, todayLow, tomorrowBuyingPrice1) && !isCapitalOver) {
            quantityBoughtToday += quantityToBeBoughtPerOrder;
            todayMoneySpent += quantityToBeBoughtPerOrder * tomorrowBuyingPrice1;
            System.out.println("Order 1 executed on:" + date + " qty:" + quantityToBeBoughtPerOrder + " costPrice:" + tomorrowBuyingPrice1 + " TodayMoneySpend:" + todayMoneySpent);
          }

          if (inbetween(todayHigh, todayLow, tomorrowBuyingPrice2) && !isCapitalOver) {

            quantityBoughtToday += quantityToBeBoughtPerOrder;
            todayMoneySpent += quantityToBeBoughtPerOrder * tomorrowBuyingPrice2;
            System.out.println("Order 2 executed on:" + date + " qty:" + quantityToBeBoughtPerOrder + " costPrice:" + tomorrowBuyingPrice2 + " TodayMoneySpend:" + todayMoneySpent);
          }

          if (inbetween(todayHigh, todayLow, tomorrowBuyingPrice3) && !isCapitalOver) {
            quantityBoughtToday += quantityToBeBoughtPerOrder;
            todayMoneySpent += quantityToBeBoughtPerOrder * tomorrowBuyingPrice3;
            System.out.println("Order 3 executed on:" + date + " qty:" + quantityToBeBoughtPerOrder + " costPrice:" + tomorrowBuyingPrice3 + " TodayMoneySpend:" + todayMoneySpent);

          }
          if (inbetween(todayHigh, todayLow, tomorrowBuyingPrice4) && !isCapitalOver) {
            quantityBoughtToday += quantityToBeBoughtPerOrder;
            todayMoneySpent += quantityToBeBoughtPerOrder * tomorrowBuyingPrice4;
            System.out.println("Order 4 executed on:" + date + " qty:" + quantityToBeBoughtPerOrder + " costPrice:" + tomorrowBuyingPrice4 + " TodayMoneySpend:" + todayMoneySpent);
          }

          if (inbetween(todayHigh, todayLow, tomorrowSellingPrice)) {
            Double profit = quantityheld * (tomorrowSellingPrice - avgCostPrice);
            netProfit += profit;
            System.out.println("Sold qty: " + quantityheld + " Profit:" + profit + " NetProfit:" + netProfit);
            quantityheld = 0;
            totalMoneySpent = 0.0;
            avgCostPrice = 0.0;
          }
        }

        totalMoneySpent += todayMoneySpent;

        int totalQtyOnHand = quantityBoughtToday + quantityheld;
        if (totalQtyOnHand > 0) {
          avgCostPrice = totalMoneySpent / totalQtyOnHand;
        }
        if(totalMoneySpent > maxCapital){
          isCapitalOver = true;
        }

        quantityheld += quantityBoughtToday;
        tomorrowSellingPrice = avgCostPrice * 1.025;


        System.out.println("=======================================i:" + i + ", qtyBought:" + quantityBoughtToday + " qtyheldEOD:"
            + quantityheld + " totalMoneySpent:" + totalMoneySpent + " avgCP:" + avgCostPrice);
        quantityBoughtToday = 0;
        System.out.println();
        if(totalMoneySpent > maxMoneySpent) {
          maxMoneySpent = totalMoneySpent;
        }
      }
      System.out.println("Net Profit:" + netProfit);
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println("Max Money:" + maxMoneySpent);
  }

  public static boolean inbetween(Double high, Double low, Double price) {

    if (high > price && price > low) {
      return true;
    }
    return false;
  }
}
