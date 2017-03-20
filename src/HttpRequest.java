import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpRequest {

	private CloseableHttpClient httpclient = HttpClients.createDefault();

	static CookieStore cookieStore = null;
	static HttpClientContext context = null;

	/**
	 * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
	 * 
	 * @return
	 */
	public String post(String url, String json) {
		String result = null;
		// 创建默认的httpClient实例.
		httpclient = HttpClients.createDefault();
		HttpResponse res = null;
		// 创建httppost
		HttpPost httppost = new HttpPost(url);

	
		try {
			StringEntity s = new StringEntity(json.toString());
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			httppost.setEntity(s);

			res = httpclient.execute(httppost);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = res.getEntity();
				result = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setContext();
		return result;
	}

	public static void setContext() {
		System.out.println("----setContext");
		context = HttpClientContext.create();
		Registry<CookieSpecProvider> registry = RegistryBuilder.<CookieSpecProvider> create()
				.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
				.register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory()).build();
		context.setCookieSpecRegistry(registry);
		context.setCookieStore(cookieStore);
	}

	/**
	 * 发送 get请求访问本地应用并根据传递参数不同返回不同结果
	 * 
	 * @return
	 */
	public String get(String url) {
		String result = null;
		// 创建默认的httpClient实例.
		// 创建httpget
		HttpGet httpget = new HttpGet(url);

		try {
			HttpResponse res = httpclient.execute(httpget);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		setContext();
		return result;
	}

	public static List<NameValuePair> getParam(Map parameterMap) {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		Iterator it = parameterMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry parmEntry = (Entry) it.next();
			param.add(new BasicNameValuePair((String) parmEntry.getKey(), (String) parmEntry.getValue()));
		}
		return param;
	}

	public String defaultpost(String url, Map parameterMap) {
		System.out.println("----testLogin");
		String result = null;
		HttpResponse res = null;


		try {
			HttpPost httpPost = new HttpPost(url);
			UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
			httpPost.setEntity(postEntity);
			res = httpclient.execute(httpPost);

			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = res.getEntity();
				result = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setContext();
		return result;
	}
	
	
	public void closehttpclient(){
		try {
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String argv[]) {
		HashMap<String, String> body = new HashMap<>();
		body.put("phone", "");
		body.put("password", "");
		HttpRequest http=new HttpRequest();
		String result = http.defaultpost("http://***/***/", body);
		System.out.println(result);
		
		System.out.println(http.get("http://***/***/"));
		http.closehttpclient();
	}

}