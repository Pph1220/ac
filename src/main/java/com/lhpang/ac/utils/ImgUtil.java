package com.lhpang.ac.utils;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
*   类路径: com.lhpang.ac.utils.ImgUtil
*   描述: 图片工具类
*   @author: lhpang
*   @date: 2019-04-25 15:50
*/
@Slf4j
public class ImgUtil {
    /**
     * 描 述: 将图片转成Base64格式
     * @date: 2019-04-25 15:50
     * @author: lhpang
     * @param: [imgPath]
     * @return: java.lang.String
     **/
    public static String ImageToBase64(String imgPath) {

        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try
        {
            in = new FileInputStream(imgPath);   //该路径只能是本机路径,不能是网络地址
            data = new byte[in.available()];
            in.read(data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } finally{
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                log.error("生成二维码异常",e);
            }
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return "data:image/png;base64,"+encoder.encode(data).replaceAll("\n|\r","");
    }
    
}
