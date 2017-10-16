package cn.mldn.util.web;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.mldn.util.MethodUtil;
import cn.mldn.util.web.exception.action.SplitUrlException;

public class ActionObjectUtil {
	private String[] actionResult;//保存拆分路径后的结果
	private HttpServletRequest request;//处理请求
	private Map<String,Class<?>> actionClassMap;
	/**
	 * 实例化Action对象的处理类对象
	 * @param actionResult 对路径拆分后的结果，该结果一定包含两个内容，一个是Action的访问路径，另外一个就是Action的方法名称
	 * @param request 用户的请求对象
	 * @param actionClassMap 包含了多有的启动时加载的Class类对象
	 * @throws SplitUrlException 如果给出的访问路径有错误，则抛出异常
	 */
	public ActionObjectUtil(String[] actionResult,HttpServletRequest request,Map<String,Class<?>> actionClassMap) throws SplitUrlException{
		if(actionResult ==null||actionResult.length!=2) {
			throw new SplitUrlException("请求路径不正确！！！");
		}
		this.actionResult = actionResult;
		this.request = request;
		this.actionClassMap = actionClassMap;
	}
	public String handleAction() throws Exception{//处理Action的反射调用   ，以下代码可能抛出异常，这里设置所有的异常都被Excpetion处理掉了
		//1.根据查分的路径信息获取请求的反射Action对象
		Object actionObject = this.actionClassMap.get(actionResult[0]).newInstance();
		Method method = MethodUtil.getMethod(this.actionClassMap.get(actionResult[0]),this.actionResult[1]);//class和方法名
		Object retObject = null;
		if(method.getParameterTypes().length==0) {//方法上没有定义参数
			System.out.println(method.getName()+".返回类型。"+method.getReturnType());
				retObject = method.invoke(actionObject);
				
		}else {	// 以下有为参数-----------------------------------这一步很关键
				retObject = ParameterValueUtil.setActionMethodParameterValue(request,actionObject, method) ;
			
		}
		if("void".equals(method.getReturnType().toString())) {
			return "nopath";
		}
		//不管有无参数的方法：都要执行以下代码
		if(retObject!=null) {
			if(retObject instanceof java.lang.String) {//返回的类型是字符串
				return retObject.toString();
			}
			if(retObject instanceof ModelAndView) {
				ModelAndView mav = (ModelAndView) retObject;
				return mav.getUrl();
			}
			
		}
		return null;
		
	}

	

}
