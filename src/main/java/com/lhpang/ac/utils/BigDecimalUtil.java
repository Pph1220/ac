package com.lhpang.ac.utils;

import java.math.BigDecimal;

/**
 * 类路径: com.lhpang.ac.utils.BigDecimalUtil
 * 描述: BigDecimal工具类
 *
 * @author: lhpang
 * @date: 2019-04-23 14:55
 */
public class BigDecimalUtil {

    private BigDecimalUtil() {

    }

    /**
     * 描 述: 加
     *
     * @date: 2019-04-23 15:01
     * @author: lhpang
     * @param: [d1, d2]
     * @return: java.math.BigDecimal
     **/
    public static BigDecimal add(double d1, double d2) {

        BigDecimal b1 = new BigDecimal(Double.valueOf(d1).toString());
        BigDecimal b2 = new BigDecimal(Double.valueOf(d2).toString());

        return b1.add(b2);
    }

    /**
     * 描 述: 减
     *
     * @date: 2019-04-23 15:04
     * @author: lhpang
     * @param: [v1, v2]
     * @return: java.math.BigDecimal
     **/
    public static BigDecimal sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    /**
     * 描 述: 乘
     *
     * @date: 2019-04-23 15:04
     * @author: lhpang
     * @param: [v1, v2]
     * @return: java.math.BigDecimal
     **/
    public static BigDecimal mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    /**
     * 描 述: 除
     *
     * @date: 2019-04-23 15:04
     * @author: lhpang
     * @param: [v1, v2]
     * @return: java.math.BigDecimal
     **/
    public static BigDecimal div(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        //四舍五入,保留2位小数
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
    }
}
