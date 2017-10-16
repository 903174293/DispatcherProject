package cn.mldn.util;

public class StringUtils {
	/**
	 * 首字母大写的方法：
	 * @param str
	 * @return
	 */
	public static String initcap(String str) {
		if(str==null||"".equals(str)) {
			return str;
		}
		if(str.length()==1) {
			return str.toUpperCase();
		}
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}

}
