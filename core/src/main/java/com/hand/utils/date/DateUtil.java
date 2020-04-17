package com.hand.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
*
* @Description 封装的一些关于日期的操作方法
*
* @author yuchao.wang
* @date 2019/8/23 14:48
*
*/
public class DateUtil {


    /**
     *
     * @Description 把日期转换成字符串 yyyy-MM-dd HH:mm:ss
     *
     * @author yuchao.wang
     * @date 2019/8/23 14:44
     * @param date 日期
     * @return java.lang.String
     *
     */
    public static String dateToString(Date date){
        String format = "yyyy-MM-dd HH:mm:ss";

        return dateToString(date, format);
    }

    /**
     *
     * @Description 把日期转换成指定格式的字符串
     *
     * @author yuchao.wang
     * @date 2019/8/23 14:44
     * @param date 日期
     * @param format 格式化字符串
     * @return java.lang.String
     *
     */
    public static String dateToString(Date date, String format){
        if(date == null)
            return null;

        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @Description 获取两个日期相隔多少天
     *
     * @author yuchao.wang
     * @date 2019/8/23 14:44
     * @param dateFrom 开始日期
     * @param dateTo 结束如期
     * @return java.lang.long
     *
     */
    public static long getDateInterval(Date dateFrom, Date dateTo){
        return (dateTo.getTime() - dateFrom.getTime()) / (1000 * 60 * 60 * 24);
    }
}