package com.hand.npi.npi_technology.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class GetNextVersion {
	public static String getVersionCode(String oldVerCode) {
		String newVerCode = "";
		if (HasDigit(oldVerCode)) {
			// 临时版本
			newVerCode = "V" + (Integer.parseInt(oldVerCode.replace("V", "")) + 1);
		} else {
			newVerCode = getNextChars(oldVerCode);
		}
		return newVerCode;
	}

	public static  String getNextChars(String letter) {
		String letterTemp = letter.trim();
		long res = converter(letterTemp);
		// 先转成数字 A=1 Z=26 AZ=52
		res++;
		String endCol = StringUtils.EMPTY;
		endCol = to26Str(res);
		return endCol;
	}

	public static String to26Str(long l) {
		StringBuffer sb = new StringBuffer();
		int jinz = 26;
		boolean f = true;
		while (f) {
			sb.insert(0, ((char) (l % jinz + 64)));
			l = l / jinz;
			if (l == 0)
				f = false;
		}
		return sb.toString();
	}

	public static long converter(String str) {
		long res = 0;
		char strArray[] = str.toCharArray();
		for (int i = strArray.length - 1, j = 0; i >= 0; i--, j++) {
			int cur = strArray[i] - 'A' + 1;
			res = res + (long) (cur * Math.pow(26, j));
		}
		return res;
	}
	
	public static boolean HasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches()) {
			flag = true;
		}
		return flag;
	}

}
