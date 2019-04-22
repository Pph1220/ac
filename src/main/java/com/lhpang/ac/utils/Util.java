package com.lhpang.ac.utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 类路径: com.lhpang.ac.utils.Util
 * 描述: 工具类
 * @author: lhpang
 * @date: 2019-04-16 21:11
 */
public class Util {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 描 述: 字符串转date
     * @date: 2019/4/22 22:08
     * @author: lhpang
     * @param:
     * @return: java.util.Date
     **/
    public static Date strToDate(String strDate,String formatStr){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = formatter.parseDateTime(strDate);

        return dateTime.toDate();
    }
    /**
     * 描 述: Date转String
     * @date: 2019/4/22 22:10
     * @author: lhpang
     * @param:
     * @return: java.lang.String
     **/
    public static String dateToString(Date date,String formatStr){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }
    /**
     * 描 述: String转date
     * @date: 2019/4/22 22:08
     * @author: lhpang
     * @param:
     * @return: java.util.Date
     **/
    public static Date strToDate(String strDate){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT);
        DateTime dateTime = formatter.parseDateTime(strDate);

        return dateTime.toDate();
    }
    /**
     * 描 述: Date转String
     * @date: 2019/4/22 22:10
     * @author: lhpang
     * @param:
     * @return: java.lang.String
     **/
    public static String dateToString(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(FORMAT);
    }

}
