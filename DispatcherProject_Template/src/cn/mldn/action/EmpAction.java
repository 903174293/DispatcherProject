package cn.mldn.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import cn.mldn.service.impl.EmpServiceImpl;
import cn.mldn.util.ActionResourceUtil;
import cn.mldn.util.web.ModelAndView;
import cn.mldn.util.web.ServletObjectUtil;
import cn.mldn.util.web.SplitPageUtil;
import cn.mldn.vo.Emp;

public class EmpAction {
	/**
	 * 实现雇员数据追加钱的控制方法
	 * @return 设置跳转的处理路径
	 * 关键注意：目前发现了一个问题：action类方法中返回值的路径：只能是page，不能是action，原因可能是getUri无法取到forward中的路径
	 * 以上问题解决了一半：跳转可以是action，也可以是page，关键取决于addPre方法，如果addPre返回String则出错，返回ModelAndView不会出错
	 * 根据新闻体统测试，证明：与返回的string或modelAndView没关系，到底为什么人员系统就不会出错呢？
	 */
	public String addPre() {//做一个增加前的跳转处理
//		return "/pages/back/admin/emp/emp_add.jsp";
		return ActionResourceUtil.getPage("emp.add.page");
	}
//	public String add(String photo,String attr, Emp vo) {
//		return "hello" ;
//	}
//	public void add(String mid, long empno, String ename, double sal, Date hiredate, int age) {
//		System.out.println("【增加雇员信息】mid = " + mid + "、empno = " + empno + "、ename = " + ename + "、sal = " + sal
//				+ "、hiredate = " + hiredate + "、age = " + age);
//	}
//	public void add(String mid,Emp emp) {//直接接收vo对象，为了说明问题，这里加上个普通的参数
//		System.out.println("【雇员增加】mid="+mid);
//		System.out.println(emp);
//	}
	public ModelAndView add(String mid,Emp emp,String inst[],long actid[]) {//本次只考虑两种数据类型的数组：String,Long(考虑到el语言不可以使用Integer)
		//以下执行验证操作：
		ModelAndView mav = new ModelAndView(ActionResourceUtil.getPage("forward.page"));
		mav.addObject("msg", ActionResourceUtil.getMessage("vo.add.success", "雇员"));
		mav.addObject("url", ActionResourceUtil.getPage("emp.add.action"));//增加成功跳转到指定的路径之下
		
		System.out.println("【雇员增加】mid="+mid);
		System.out.println("【雇员增加】雇员兴趣："+Arrays.toString(inst));
		System.out.println("【雇员增加】雇员权限："+Arrays.toString(actid));
		System.out.println(emp);
		String fileName = ServletObjectUtil.getParam().createUploadFileName("photo").get(0) ;
		String filePath = ServletObjectUtil.getApplication().getRealPath("/upload/emp/photo/") + fileName ;
		System.out.println("新的图片名称：" + filePath);
		ServletObjectUtil.getParam().saveUploadFile("photo", filePath);
		return mav;//---------------------------------------------------------返回即可跳转
	}
	public void print() {//关键注意:该方法：ajax处理的时候使用非常方便：ajax处理的请求”页面“返回的json或者text将全部交给回掉函数data
		try {
			ServletObjectUtil.getResponse().getWriter().println("<h1>www.mldn.cn</h1>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ModelAndView editPre() {
		ModelAndView mav = new ModelAndView("/pages/back/admin/emp/emp_edit.jsp");
		mav.addObject("msg", "www.mldn.cn");
		return mav;
	}
	public ModelAndView list() {//做一个增加前的页面跳转处理
		//将跳转的路径信息设置到ModelAndView之中
		ModelAndView mav = new ModelAndView(ActionResourceUtil.getPage("emp.list.page"));
		SplitPageUtil spu = new SplitPageUtil("雇员姓名:ename","emp.list.action");
		EmpServiceImpl empService = new EmpServiceImpl();
		Map<String,Object> map = empService.list(spu.getColumn(), spu.getKeyWord(), spu.getCurrentPage(), spu.getLineSize());
		mav.addObjectMap(map);//直接将map集合的内容遍历后保存在request范围内
		return mav;
	}
}
