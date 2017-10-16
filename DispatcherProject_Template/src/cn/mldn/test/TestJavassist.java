package cn.mldn.test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import cn.mldn.action.EmpAction;
import cn.mldn.util.MethodUtil;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo; 

public class TestJavassist {
	private static final String METHOD_NAME = "add" ;	// 要操作的方法名称
	public static void main(String[] args) throws Exception {
		Class<?> cls = EmpAction.class ;	// 获得要操作Action的Class类的对象，获得这个类对象是为了进行反射处理操作
		Method method = MethodUtil.getMethod(cls, METHOD_NAME) ;	// 取得你要进行操作的方法
		Class<?> params [] = method.getParameterTypes() ;	// 方法中全部参数的类型
		System.out.println(Arrays.toString(params));
		// 随后如果要想获取参数的名称信息，则就需要使用Javassist开发包之中所给的类进行处理了
		ClassPool classPool = ClassPool.getDefault() ; // 定义要进行解析处理的工具类
		CtClass ctClass = classPool.get(cls.getName()) ;	// 定义要操作的字节码文件，这个文件一定在CLASSPATH中
		// 获取要进行字节码解析部分的class的二进制数据信息
		CtMethod ctMethod = ctClass.getDeclaredMethod(METHOD_NAME) ;	// 获取操作的方法信息
		MethodInfo methodInfo = ctMethod.getMethodInfo() ;	// 获取要解析的二进制的方法具体内容
		// 解析二进制的字节码文件获取相关全部属性内容
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute() ;	// 得到源代码之中定义的相关属性信息
		// 现在需要的是方法中参数的名称，参数名称就属于一个标记，所有的参数信息都在attribute之中
		LocalVariableAttribute attribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag) ;
		int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1 ;
		for (int x = 0 ; x < params.length ; x ++) {
			System.out.println(attribute.variableName(x + pos)); // 根据参数定义的顺序取出参数的名称
		}
	}
}
