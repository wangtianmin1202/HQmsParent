package com.hand.hap.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.support.RequestContextUtils;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.PromptServiceImpl;

import jodd.util.StringUtil;

/**
 * @author tainmin.wang
 * @version date：2019年7月31日 上午10:46:22 常用方法类
 */
public class SystemApiMethod {

	/**
	 * 获取保存文件的已配置服务器根目录 以/demension访问
	 * 
	 * @return
	 */
	private SystemApiMethod() {
	}

	public static Date getThisWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 获得当前日期是一个星期的第几天
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int day = cal.get(Calendar.DAY_OF_WEEK);
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		return cal.getTime();
	}

	public static String getFileFolder() {
		String rootPath = "/apps/hap/resource";
		String path = rootPath;
		if (SystemApiMethod.getOsType().equals("window")) {
			rootPath = "C:/apps/hap/resource";
			path = rootPath;
		}
		return path;
	}

	/**
	 * 获取描述默认中文
	 * 
	 * @param iPromptService
	 * @param code
	 * @return
	 */
	public static String getPromptDescription(IPromptService iPromptService, String code) {
		String returnString = "first info";
		try {
			String localLanguage = "zh_CN";
			returnString = iPromptService.getPromptDescription(localLanguage, code);
		} catch (Exception e) {
			returnString = code;
		}
		if (returnString == null || returnString.equals("")) {
			returnString = code;
		}
		return returnString;
	}

	public static String getPromptDescription(String code) {
		IPromptService promptServiceImpl = (IPromptService) ContextLoader.getCurrentWebApplicationContext()
				.getBean("promptServiceImpl");
		String returnString = "first info";
		try {
			String localLanguage = "zh_CN";
			returnString = promptServiceImpl.getPromptDescription(localLanguage, code);
		} catch (Exception e) {
			returnString = code;
		}
		if (returnString == null || returnString.equals("")) {
			returnString = code;
		}
		return returnString;
	}

	/**
	 * 
	 * @param HttpServletRequest request 为空时默认为中文
	 * @param iPromptService     动态注入IPromptService传入即可
	 * @param code               编码值
	 * @return
	 */
	public static String getPromptDescription(HttpServletRequest request, IPromptService iPromptService, String code) {
		String returnString = "first info";
		try {
			String localLanguage = "zh_CN";
			if (request != null) {
				localLanguage = RequestContextUtils.getLocale(request).getLanguage().equals("zh") ? "zh_CN" : "en_GB";
			}
			returnString = iPromptService.getPromptDescription(localLanguage, code);
		} catch (Exception e) {
			returnString = code;
		}
		if (returnString == null || returnString.equals("")) {
			returnString = code;
		}
		return returnString;
	}

	/**
	 * 
	 * @param IRequest       request
	 * @param iPromptService
	 * @param code
	 * @return
	 */
	public static String getPromptDescription(IRequest request, IPromptService iPromptService, String code) {
		String returnString = "first info";
		try {
			String localLanguage = "zh_CN";
			if (request != null) {
				localLanguage = request.getLocale().contains("zh") ? "zh_CN" : "en_GB";
			}
			returnString = iPromptService.getPromptDescription(localLanguage, code);
		} catch (Exception e) {
			returnString = code;
		}
		if (returnString == null || returnString.equals("")) {
			returnString = code;
		}
		return returnString;
	}

	/**
	 * 获取当前操作系统类型
	 * 
	 * @param request
	 * @param iPromptService
	 * @param code
	 * @return
	 */
	public static String getOsType() {
		Properties props = System.getProperties();
		if (props.getProperty("os.name").toLowerCase().contains("window")) {
			return "window";
		} else {
			return "linux";
		}

	}

	/**
	 * 判断传入的par2是否与par1的精度一样(小数点后的位数)
	 * 
	 * @return
	 */
	public static boolean judgePercision(String pa1, String pa2) {
		if (getPercision(pa1) == getPercision(pa2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 当前传入字符串的小数点后的数字位数
	 * 
	 * @param par
	 * @return
	 */
	public static int getPercision(String par) {
		if (StringUtils.isEmpty(par))
			return -1;
		if (par.contains(".") && (par.split("\\.")).length > 1) {
			return par.split("\\.")[1].length();
		} else {
			return 0;
		}
	}

	public static String removeZero(String in) {
		if (StringUtil.isEmpty(in)) {
			return "";
		}
		if (in.matches("^[0-9]*$")) {
			try {
				return String.valueOf(Integer.valueOf(in));
			} catch (Exception e) {
				return "-1";
			}
		} else {
			return in;
		}
	}

	// 驼峰转下划线
	public static String camelToUnderline(String param, Integer charType) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append('_');
			}
			if (charType == 2) {
				sb.append(Character.toUpperCase(c)); // 统一都转大写
			} else {
				sb.append(Character.toLowerCase(c)); // 统一都转小写
			}

		}
		return sb.toString();
	}

	// 下划线转驼峰
	public static String underlineToCamel(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		Boolean flag = false; // "_" 后转大写标志,默认字符前面没有"_"
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c == '_') {
				flag = true;
				continue; // 标志设置为true,跳过
			} else {
				if (flag == true) {
					// 表示当前字符前面是"_" ,当前字符转大写
					sb.append(Character.toUpperCase(param.charAt(i)));
					flag = false; // 重置标识
				} else {
					sb.append(Character.toLowerCase(param.charAt(i)));
				}
			}
		}
		return sb.toString();
	}
}
