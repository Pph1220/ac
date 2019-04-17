package com.lhpang.ac.utils;

import java.security.MessageDigest;

/**
 * 类路径: com.lhpang.ac.utils.MD5Util
 * 描述: md5加密
 * @author: lhpang
 * @date: 2019-04-17 21:44
 */
public class MD5Util {

    /**
     * 描 述: md5加密
     * @date: 2019/4/17 21:32
     * @author: lhpang
     * @param:
     * @return:
     **/
    public static String md5(String string ){

        return encodeByMD5(string);
    }

    private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

    private static String encodeByMD5(String originString){
        if (originString!=null) {
            try {
                //创建具有指定算法名称的信息摘要
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md5.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                String result = byteArrayToHexString(results);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        for(int i=0;i<b.length;i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b){
        int n = b;
        if(n<0) {
            n = 256 + n;
        }
        int d1 = n/16;
        int d2 = n%16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
