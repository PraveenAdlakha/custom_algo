package com.pa;

import com.pa.service.StockService;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

@SpringBootApplication
public class StockApplication {

  public static final String KITE_ACCESS_TOKEN = "kite.accessToken";
  public static final String KITE_PUBLIC_TOKEN = "kite.publicToken";


  @Autowired
  private KiteConnect kiteConnect;

  @Autowired
  private StockService stockService;

  @Bean
  public KiteConnect kiteConnect() {
    KiteConnect kiteSdk = new KiteConnect(API_KEY);
    kiteSdk.setUserId(USER_ID);
    return kiteSdk;
  }

  public static void main(String[] args) {
    SpringApplication.run(StockApplication.class, args);
  }

  @PostConstruct
  public void start() throws KiteException, IOException {

    String accessToken = read(KITE_ACCESS_TOKEN);
    String publicToken = read(KITE_PUBLIC_TOKEN);

    if (accessToken == null || publicToken == null) {
      login();
      return;
    }
    kiteConnect.setAccessToken(accessToken);
    kiteConnect.setPublicToken(publicToken);

    try {
      String email = kiteConnect.getProfile().email;
      System.out.println("Looks like logged in. Successfully retrieved email id " + email);
      StaggeredBuyingAndSelling stag = new StaggeredBuyingAndSelling();
      String [] symbol = {"NSE:BAJFINANCE"};
      System.out.println("trying to place order...");
  //    stag.placeStaggeredAmoBuyOrderOnCamarillaSupport(symbol, 3);
      //stockService.placeAmoOrder();
      stockService.f();
    } catch (Throwable e) {
      e.printStackTrace();
     // login();
    }

  }

  private void login() throws KiteException, IOException {
    String url = kiteConnect.getLoginURL();
    System.out.println(url);
    Scanner scanner = new Scanner(System.in);
    String token = scanner.next();
    User user = kiteConnect.generateSession(token, API_SECRET);
    System.out.println(user.accessToken);
    System.out.println(user.publicToken);
    store(KITE_ACCESS_TOKEN, user.accessToken);
    store(KITE_PUBLIC_TOKEN, user.publicToken);

  }

  private void store(String key, String value) throws IOException {
    try (FileWriter writer = new FileWriter("/tmp/" + key, false)) {
      writer.write(value);
    }
  }

  private String read(String key) throws IOException {
    try (Scanner reader = new Scanner(Paths.get("/tmp/", key))) {
      return reader.next();
    } catch (Exception e) {
      return null;
    }

  }
}

