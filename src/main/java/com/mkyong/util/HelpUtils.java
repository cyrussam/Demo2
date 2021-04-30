//package org.ibase4j.mazex;
package com.mkyoung.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;

import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

//import org.ibase4j.mazex.ObjResp;
//import org.ibase4j.mazex.Resp;
//import org.ibase4j.mazex.CoinRecharge;
//import com.mkyoung.util.BaseSecretReq;
import com.mkyoung.util.MacMD5;
import com.mkyoung.util.SecurityUtil;

import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.codec.digest.HmacAlgorithms;
//import org.apache.commons.codec.digest.HmacUtils;



public class HelpUtils { //extends StringUtils {

    private static char[] numbersAndLetters = ("23456789ABCDEFGHJKMNPQRSTUVWXYZ").toCharArray();

    public static String createSign(Map<String, Object> params, String apiSecret) {

        System.out.println("call createSign func");

        SortedMap<String, Object> sortedMap = new TreeMap<>(params);
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, Object>> es = sortedMap.entrySet();
        Iterator<Map.Entry<String, Object>> it = es.iterator();
    
	    while (it.hasNext()) {
            Map.Entry entry = it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if (v != null && !"".equals(v) && !"sign".equals(k))
                sb.append(String.valueOf(k) + "=" + v + "&"); 
        } 
        if ("MD5".equals(params.get("sign_type"))) {
            sb.append("apiSecret=" + apiSecret);
        } else {
            sb.deleteCharAt(sb.length() - 1);
        } 

        String payload = sb.toString();
        String actualSign = "";
        if ("MD5".equals(params.get("sign_type"))) {

            System.out.println("createSign - payload =" +  payload);   
            actualSign = MacMD5.CalcMD5(payload, 28);
            System.out.println("createSign - Sign" + actualSign);

        } else 
        {
            Mac hmacSha256 = null;
            try {
                hmacSha256 = Mac.getInstance("HmacSHA256");
                SecretKeySpec secKey = new SecretKeySpec(
                    apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
                    hmacSha256.init(secKey);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("No such algorithm: " + e.getMessage());
            } catch (InvalidKeyException e) {
                throw new RuntimeException("Invalid key: " + e.getMessage());
            } 
            byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            actualSign = Base64.encodeBase64String(hash);
            actualSign = MacMD5.CalcMD5(actualSign, 28); 
        } 
        return actualSign;
    }

    public static String formatDateByFormatStr(Date myDate, String formatStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        return formatter.format(myDate);
    }

    public static String genToken(final String mName, final Integer id) {
        return String.valueOf(MacMD5.CalcMD5(MacMD5.CalcMD5(String.valueOf(mName) + "Pm,ZhongGuo,Ex." + id, 32), 32))
                + Integer.toHexString(id.intValue());
    }

    public static String formatDate8(Date myDate) {
        return formatDateByFormatStr(myDate, "yyyy-MM-dd HH:mm:ss");
    }

    public static final int getRandom(int maxRandom) {
        return (int)((1.0D - Math.random()) * maxRandom);
    }

	/**
	 * 取得当前时间戳（精确到秒）
	 * 
	 * @return nowTimeStamp
	 */
	public static Integer getNowTimeStampInt() {
		long time = System.currentTimeMillis();
		Integer nowTimeStamp = Integer.parseInt(String.valueOf(time / 1000));
		return nowTimeStamp;
	}

    public static final String randomString(int length) {
        if (length < 1)
          return null; 
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++)
          randBuffer[i] = numbersAndLetters[getRandom(30)]; 
        return new String(randBuffer);
    }

    public static <T> Map<String, Object> objToMap(T t) {
        Map<String, Object> params = new HashMap<>();
        Class<?> clazz = t.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields = clazz.getDeclaredFields();
                for (int j = 0; j < fields.length; j++) {
                    String name = fields[j].getName();
                    Object value = null;
                    Method method = t.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1), new Class[0]);
                    value = method.invoke(t, new Object[0]);
                    if (value != null)
                        params.put(name, value); 
                } 
            } catch (Exception exception) {}
        } 
        return params;
    }
}