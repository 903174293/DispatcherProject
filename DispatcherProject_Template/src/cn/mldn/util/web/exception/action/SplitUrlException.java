package cn.mldn.util.web.exception.action;

@SuppressWarnings("serial")
public class SplitUrlException extends Exception{
/**
 * 这里的异常是架构设计人员可以读懂的异常，跟业务层定义的异常意义不同，业务层是项目使用者定义的异常
 * @param msg
 */
	public SplitUrlException(String msg) {
		super(msg);
	}
}
