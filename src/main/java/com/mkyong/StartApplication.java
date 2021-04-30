package com.mkyong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.regex.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
//import java.util.Base64;
import java.util.UUID;


@SpringBootApplication
public class StartApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

    static public byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
      byte[] hmacSha256 = null;
      try {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
        mac.init(secretKeySpec);
        hmacSha256 = mac.doFinal(message);
      } catch (Exception e) {
        throw new RuntimeException("Failed to calculate hmac-sha256", e);
      }
      return hmacSha256;
    }

    @Override
    public void run(String... args) {

      System.out.println("StartApplication...");   

  /*    
      try {
        String secretKey = "123456";
        String urldata = "http://openapi.mazex.io/account/accountbalance?SignatureMethod=HmacSHA256&SignatureVersion=1&Timestamp=2020-08-22T12:01:31.414+08:00&dc_code=MPV&accessKey=123456";
        byte[] hmacSha256 = calcHmacSha256(secretKey.getBytes("UTF-8"), urldata.getBytes("UTF-8"));
        String encodedString = java.util.Base64.getEncoder().encodeToString(hmacSha256);
        System.out.println(encodedString); 
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      
  */
      
      try {
        String secret = "123456";
        String message = "http://openapi.mazex.io/account/accountbalance?SignatureMethod=HmacSHA256&SignatureVersion=1&Timestamp=2020-08-22T12:01:31.414+08:00&dc_code=MPV&accessKey=123456";

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        String hash =  org.apache.commons.codec.binary.Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
        System.out.println(hash);
      }
      catch (Exception e){
        System.out.println("Error");
      }
        

    } 

}