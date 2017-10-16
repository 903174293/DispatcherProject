package cn.mldn.util.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 该类主要是为modelAndView类服务，项目启动的时候初始化，application和config，而执行的时候doPost，初始化request和response
 * @author Maibenben
 *
 */
public class ServletObjectUtil {
	private static ServletContext application;
	private static ServletConfig config;
	private static final ThreadLocal<HttpServletRequest> THREADLOCAL_REQUEST = new ThreadLocal<HttpServletRequest>();
	private static final ThreadLocal<HttpServletResponse> THREADLOCAL_RESPONSE = new ThreadLocal<HttpServletResponse>();
	private static final ThreadLocal<ParameterUtil> THREADLOCAL_PARMUTIL = new ThreadLocal<ParameterUtil>();
	public static void setParam(ParameterUtil pu) {
		THREADLOCAL_PARMUTIL.set(pu);
	}
	public static void setApplication(ServletContext app) {
		 application = app;
	}
	public static void setConfig(ServletConfig conf) {
		config = conf;
	}
	public static void setRequest(HttpServletRequest req) {
		THREADLOCAL_REQUEST.set(req);
	}
	public static void setResponse(HttpServletResponse resp) {
		THREADLOCAL_RESPONSE.set(resp);
	}
	public static ServletContext getApplication() {
		return application;
	}
	public static ServletConfig getConfig() {
		return config;
	}
	public static HttpServletRequest getRequest() {
		return THREADLOCAL_REQUEST.get();
	}
	public static HttpServletResponse getResponse() {
		return THREADLOCAL_RESPONSE.get();
	}
	public static ParameterUtil getParam() {
		return THREADLOCAL_PARMUTIL.get();
	}
	public static void clear() {
		THREADLOCAL_REQUEST.remove();
		THREADLOCAL_RESPONSE.remove();
	}

}
