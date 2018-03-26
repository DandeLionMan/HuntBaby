package com.dandelion.utils;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;
/**
 * Created by qing on 2017/8/2.
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 */

public class AES {

    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private String sKey = "smkldospdosldaaa";//key，可自行修改
    private String ivParameter = "0392039203920300";//偏移量,可自行修改
    private static AES instance = null;
    
    private static final String IV_STRING = "16-Bytes--String";

    private AES() {

    }

    public static AES getInstance() {
        if (instance == null)
            instance = new AES();
        return instance;
    }


    public static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static String Encrypt(String encData, String secretKey, String vector) throws Exception {
//
//        if (secretKey == null) {
//            return null;
//        }
//        if (secretKey.length() != 16) {
//            return null;
//        }
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        byte[] raw = secretKey.getBytes();
//        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//        IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
//        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//        byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
//        return Base64.encodeToString(encrypted, Base64.DEFAULT);// 此处使用BASE64做转码。
//    }

    // 加密
    public String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return encodeBase64(encrypted);// 此处使用BASE64做转码。
    }

    // 解密
    public String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = decodeBase64(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static byte[] decodeBase64(String input) throws Exception{  
        Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");  
        Method mainMethod= clazz.getMethod("decode", String.class);  
        mainMethod.setAccessible(true);  
         Object retObj=mainMethod.invoke(null, input);  
         return (byte[])retObj;  
    }
    
    /*** 
     * encode by Base64 
     */  
    public static String encodeBase64(byte[]input) throws Exception{  
        Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");  
        Method mainMethod= clazz.getMethod("encode", byte[].class);  
        mainMethod.setAccessible(true);  
         Object retObj=mainMethod.invoke(null, new Object[]{input});  
         return (String)retObj;  
    }  

//    public String decrypt(String sSrc, String key, String ivs) throws Exception {
//        try {
//            byte[] raw = key.getBytes("ASCII");
//            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//            byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);// 先用base64解密
//            byte[] original = cipher.doFinal(encrypted1);
//            String originalString = new String(original, "utf-8");
//            return originalString;
//        } catch (Exception ex) {
//            return null;
//        }
//    }

    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }
    
    
    public static String encryptAES(String content, String key) throws InvalidKeyException, NoSuchAlgorithmException, 
            NoSuchPaddingException, UnsupportedEncodingException, 
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

    byte[] byteContent = content.getBytes("UTF-8");

    // 注意，为了能与 iOS 统一
    // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
    byte[] enCodeFormat = key.getBytes();
    SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");

    byte[] initParam = IV_STRING.getBytes();
    IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

    // 指定加密的算法、工作模式和填充方式
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

    byte[] encryptedBytes = cipher.doFinal(byteContent);

    // 同样对加密后数据进行 base64 编码
    Encoder encoder = Base64.getEncoder();
    return encoder.encodeToString(encryptedBytes);
}


}
