//package org.ibase4j.mazex;
//packagecom.pmzhongguo.ex.core.utils;
package com.mkyoung.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MacMD5 {
   private static byte[] digesta;

   public static String CalcMD5(String myinfo) {
      return CalcMD5(myinfo, 15);
   }

   public static String CalcMD5Member(String myinfo) {
      return CalcMD5(myinfo, 32);
   }

   public static String BothCalcMD5(String myinfo) {
      return CalcMD5(CalcMD5(myinfo, 32) + "MOVESAY", 32);
   }

   public static String CalcMD5(String myinfo, int length) {
      try {
         MessageDigest alga = MessageDigest.getInstance("MD5");
         alga.update(myinfo.getBytes());
         digesta = alga.digest();
      } catch (NoSuchAlgorithmException var3) {
         var3.printStackTrace();
      }

      return byte2hex(digesta, length);
   }

   private static String byte2hex(byte[] b, int length) {
      String hs = "";
      String stmp = "";

      for(int n = 0; n < b.length; ++n) {
         stmp = Integer.toHexString(b[n] & 255);
         if (stmp.length() == 1) {
            hs = hs + "0" + stmp;
         } else {
            hs = hs + stmp;
         }
      }

      return hs.substring(0, length);
   }

   public static void main(String[] args) {
      System.out.println(CalcMD5("123456", 32));
      System.out.println(CalcMD5("123456"));
      System.out.println(CalcMD5(CalcMD5("123456", 32) + "MOVESAY", 32));
   }
}