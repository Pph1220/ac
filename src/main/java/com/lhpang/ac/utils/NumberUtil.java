package com.lhpang.ac.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 类路径: com.lhpang.ac.utils.NumberUtil
 * 描述: 工具类
 * @author: lhpang
 * @date: 2019-05-02 13:11
 */
@Slf4j
public class NumberUtil {
    /**
     * 描 述: 将123,456,789转为long
     * @date: 2019/5/2 13:16
     * @author: lhpang
     * @param: [string]
     * @return: long
     **/
    public static long stringToLong(String string){

        String[] strings = string.split(",");

        StringBuffer stringBuffer = new StringBuffer();

        for(int i =0 ; i < strings.length ; i++){
            stringBuffer.append(strings[i]);
        }

        return Long.parseLong(stringBuffer.toString());

    }
}
