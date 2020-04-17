package com.hand.spc.utils;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utils
 */
public class Utils {

    private Utils() {
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 天数
     */
    public static int differentDaysByMillisecond(Date startDate, Date endDate) {
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 秒数
     */
    public static int differentMillisecond(Date startDate, Date endDate) {
        int seconds = (int) ((endDate.getTime() - startDate.getTime()) / 1000);
        return seconds;
    }

    /**
     * 日志拼接
     *
     * @param uuid
     * @param logText
     * @return
     */
    public static String getLog(String uuid, String logText) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return "[UUID=" + uuid.replace("-", "") + "] - [date=" + f.format(new Date()) + "] - " + logText;
    }

    /**
     * 向txt中写入内容，并且可以选择在原来的基础上追加新内容，也可以覆盖旧内容
     *
     * @param content
     * @param filePath
     * @param append
     * @return
     */
    public static boolean writeTxtFile(String content, String filePath, boolean append) {
        boolean flag = false;
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(filePath, append);
            fw.write(content);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }
}
