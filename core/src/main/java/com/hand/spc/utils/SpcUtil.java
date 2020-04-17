package com.hand.spc.utils;

public class SpcUtil {
    /**
     * @Author han.zhang
     * @Description 给序列拼接0
     * @Date 上午10:58 2019/10/17
     * @Param []
     **/
    public static String getZeroNumber(Long number,int length){
        if(null == number){
            number = 1L;
        }
        int numLength = number.toString().length();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length-numLength; i++) {
            stringBuilder.append('0');
        }
        stringBuilder.append(number);
        return stringBuilder.toString();
    }
}
