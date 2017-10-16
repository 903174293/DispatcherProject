package cn.mldn.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.mldn.util.web.ServletObjectUtil;

public class DataConverterUtil {
	/**
	 * 将字符串数组进行指定格式的转换
	 * @param value 字符串数组
	 * @param type 转换类型
	 * @return 转换后的数组参数
	 */
	public static Object converterArray(String value[],String type) {//关键注意：字符串数组无需转换，request.getParameter得到的就是字符串的数组
		Object result = value;
		if("long[]".equals(type)||"java.lang.Long[]".equals(type)) {
			long arr[] = new long[value.length];
			for(int x=0;x<value.length;x++) {
				arr[x] = Long.parseLong(value[x]);
			}
			result =arr;
		}
		return result;
	}
	/**
	 * 进行vo的实例化处理操作，同时设置请求参数
	 * @param voClass 要执行的vo类型
	 * @return 实例化的vo对象
	 */
	public static Object converterVO(Class<?> voClass) {
		Object vo = null;
		try {
			vo = voClass.newInstance();//实例化VO类对象
			Field fields[] = voClass.getDeclaredFields();//获取全部的属性信息
			for(int x=0;x<fields.length;x++) {
				String paramName = fields[x].getName();
				Method method = voClass.getMethod("set"+StringUtils.initcap(paramName), fields[x].getType());
//				method.invoke(vo, converter(ServletObjectUtil.getRequest().getParameter(paramName),fields[x].getType().getName()));//通过反射，反复调用setter方法给vo属性赋值---------------关键
				//以下为表单封装后参数的接收
				method.invoke(vo, converter(ServletObjectUtil.getParam().getParameter(paramName),fields[x].getType().getName()));//通过反射，反复调用setter方法给vo属性赋值---------------关键
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
	/**
	 * 实现字符串与具体类型之间的转换处理操作
	 * @param value 表示接收的字符串参数
	 * @param type 表示转换目标类型
	 * @return 转换后的数据结果
	 */
	public static Object converter(String value,String type) {
		Object obj = value ;	// 保存最终转换类型
		if ("int".equals(type) || "java.lang.Integer".equals(type)) {
			obj = Integer.parseInt(value) ;
		}
		if ("long".equals(type) || "java.lang.Long".equals(type)) {
			obj = Long.parseLong(value);
		}
		if ("double".equals(type) || "java.lang.Double".equals(type)) {
			obj = Double.parseDouble(value) ;
		}
		if ("java.util.Date".equals(type)) {
			SimpleDateFormat sdf = null ;
			if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
				sdf = new SimpleDateFormat("yyyy-MM-dd") ;
			} 
			if (value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
			}
			if (sdf != null) {
				try {
					obj = sdf.parse(value) ;
				} catch (ParseException e) {
				}
			}
		}
		return obj ;
	}
}
