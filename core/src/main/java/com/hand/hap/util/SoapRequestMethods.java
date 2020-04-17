/**
 * 
 */
package com.hand.hap.util;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.io.SAXReader;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
/**
* @author tainmin.wang
* @version date：2020年1月7日 下午6:18:41
* 
*/
public class SoapRequestMethods {
	
	public static String RequestHttp(String soapxml, String uri) throws Exception {
		SAXReader reader = new SAXReader();
		try {
			String method = uri;// 比如 http://cnsbx034:7005/WMS_Provide_PS/WMSServicePipelineProxyService
			PostMethod postMethod = new PostMethod(method);
			byte[] b = soapxml.getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is, b.length, "application/soap+xml; charset=utf-8;");
			postMethod.setRequestHeader("SOAPAction", "");
			postMethod.setRequestEntity(re);
			HttpClient httpClient = new HttpClient();
			Integer statusCode = httpClient.executeMethod(postMethod);
			byte[] responseBody = postMethod.getResponseBody();
			return new String(responseBody);
		} catch (Exception e) {
			throw e;
		}
	}

	public static String RequestHttps(String soapXml, String uri) throws Exception {
		String oaRestfulServiceUrl = null;
		// am.getOADBTransaction().getProfile("");
		oaRestfulServiceUrl = uri;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000)
				.build();
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse h_response = null;
		try {
			httpclient = new SSLClient();
			HttpPost postMethod = new HttpPost(oaRestfulServiceUrl);
			// postMethod.setConfig(requestConfig);
			postMethod.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
			postMethod.setHeader("Referer", oaRestfulServiceUrl);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			postMethod.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			postMethod.setHeader("Content-Type", "text/xml;charset=UTF-8");
			postMethod.setHeader("SOAPAction", "");
			StringEntity data = new StringEntity(soapXml, Charset.forName("UTF-8"));
			postMethod.setEntity(data);
			h_response = httpclient.execute(postMethod);
			HttpEntity repEntity = h_response.getEntity();
			int statusCode = h_response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				postMethod.abort();
			}
			String content = EntityUtils.toString(repEntity, "UTF-8");
			return content;
		} catch (Exception e) {
			throw e;
		} finally {
			if (h_response != null) {
				try {
					h_response.close();
				} catch (IOException e) {
					throw e;
				}
			}
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	public static class SSLClient extends DefaultHttpClient {
		public SSLClient() throws Exception {
			super();
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = this.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));
		}
	}
}
