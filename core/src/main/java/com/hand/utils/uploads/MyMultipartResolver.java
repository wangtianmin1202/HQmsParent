package com.hand.utils.uploads;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @Description: //TODO 处理上传 MultipartResolver 和 ServletFileUpload上传冲突问题
 */
public class MyMultipartResolver extends CommonsMultipartResolver {
	private List<String> excludeUrls;

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	/**
	 * @param excludeUrls bean注入url
	 */
	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	/**
	 * 这里是处理Multipart http的方法。如果这个返回值为true,那么Multipart http
	 * body就会MyMultipartResolver 消耗掉.如果这里返回false
	 * 那么就会交给后面的自己写的处理函数处理例如刚才ServletFileUpload 所在的函数
	 *
	 * @see CommonsMultipartResolver#isMultipart(HttpServletRequest)
	 */
	@Override
	public boolean isMultipart(HttpServletRequest request) {
		// 非APP请求，跳过解析 Multipart http的方法
		for (String s : getExcludeUrls()) {
			String requestURI = request.getRequestURI();
			if (!requestURI.contains(s)) {
				return false;
			}
		}

		return super.isMultipart(request);
	}
}