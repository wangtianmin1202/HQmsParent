package com.hand.plm.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

public class Util {

	/**
	 * 将指定的自然数转换为26进制表示。映射关系：[1-26] ->[A-Z]
	 * 
	 * @param n 自然数（如果无效，则返回空字符串）
	 * @return 26进制表示
	 */
	public static String toNumberSystem26(int n) {
		String s = "";
		while (n > 0) {
			int m = n % 26;
			if (m == 0)
				m = 26;
			s = (char) (m + 64) + s;
			n = (n - m) / 26;
		}
		return s;
	}

	/**
	 * 将指定的26进制表示转换为自然数。映射关系：[A-Z] ->[1-26]。
	 * 
	 * @param s 26进制表示（如果无效，则返回0）
	 * @return 自然数
	 */
	public static int fromNumberSystem26(String s) {
		if (StringUtils.isEmpty(s)) {
			return 0;
		}

		int n = 0;
		for (int i = s.length() - 1, j = 1; i >= 0; i--, j *= 26) {
			char c = s.charAt(i);
			if (c < 'A' || c > 'Z') {
				return 0;
			}
			n += ((int) c - 64) * j;
		}
		return n;
	}
	
	/**
     * 将null对象转化为""
     *
     * @param bean
     * @param <T>
     */
    public static <T> void nullConverNullString(T bean) {
        Field[] field = bean.getClass().getDeclaredFields();
        //遍历集合属性
        for (int i = 0; i < field.length; i++) {
            String name = field[i].getName();
            //获得属性的首字符大写，方便构造get,set方法
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            //获取属性类型
            String type = field[i].getGenericType().toString();
            //如果type是类类型，则前面包含"clas",后面跟类名
            if (type.equals("class java.lang.String")) {
                try {
                    Method mGet = bean.getClass().getMethod("get" + name);
                    //调用getter方法获取属性值
                    String value = (String) mGet.invoke(bean);
                    if (value == null || "".equals(value) || "null".equals(value)) {
                        Method mSet = bean.getClass().getMethod("set" + name, new Class[]{String.class});
                        mSet.invoke(bean, new Object[]{new String("")});
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * double 数据，去掉末尾.0
     *
     * @param val
     * @return
     */
    public static String convertDoubleToString(Double val) {
        if (val == null) {
            return "";
        } else {
            BigDecimal bd = new BigDecimal(String.valueOf(val));
            return bd.stripTrailingZeros().toPlainString();
        }
    }
    /**
     * Float 数据，去掉末尾.0
     *
     * @param val
     * @return
     */
    public static String convertFloatToString(Float val) {
        if (val == null) {
            return "";
        } else {
            BigDecimal bd = new BigDecimal(String.valueOf(val));
            return bd.stripTrailingZeros().toPlainString();
        }
    }
    /**
     * 校验8为字符串是否为正确的日期格式
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static boolean checkDate(String dateStr) {
        boolean result = true;
        if (dateStr.length() != 8) {
            result = false;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            try {
                format.setLenient(false);
                format.parse(dateStr);
            } catch (Exception ex) {
                result = false;
            }
        }
        return result;
    }
}
