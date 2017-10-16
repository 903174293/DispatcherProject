package cn.mldn.test;

import java.lang.reflect.Method;

import cn.mldn.action.EmpAction;
import cn.mldn.util.MethodUtil; 

public class TestMethodUtil {
	public static void main(String[] args) {
		Method met = MethodUtil.getMethod(EmpAction.class, "add") ;
		System.out.println(met);
	}
}
