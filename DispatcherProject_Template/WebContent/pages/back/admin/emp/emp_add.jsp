<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="cn.mldn.vo.*" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
	String emp_add_url = basePath + "pages/back/admin/emp/EmpAction!add.action" ;
%>
<html>
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>DispatcherServlet管理</title>
</head>
<body>
<h1>增加雇员信息、${errors}</h1>
<form action="<%=emp_add_url%>" method="post" enctype="multipart/form-data">
	操作用户：<input type="text" name="mid" value="mldnjava"/><br/>
	雇员编号：<input type="text" name="empno" value="7369"/><br/>
	雇员姓名：<input type="text" name="ename" value="约翰"/><br/>
	基本工资：<input type="text" name="sal" value="1.1"/><br/>
	雇佣日期：<input type="text" name="hiredate" value="1998-11-11 11:11:11"/><br/>
	雇员年龄：<input type="text" name="age" value="20"/><br/>
	雇员兴趣：<input type="checkbox" name="inst" value="唱歌" checked>唱歌
			<input type="checkbox" name="inst" value="跳舞" checked>跳舞
			<input type="checkbox" name="inst" value="看书" checked>看书<br>
	雇员权限：<input type="checkbox" name="actid" value="1" checked>增加部门
			<input type="checkbox" name="actid" value="2" checked>部门列表
			<input type="checkbox" name="actid" value="3" checked>公告管理<br>
	雇员照片：<input type="file" name="photo"><br>
	<input type="submit" value="增加"/>
	<input type="reset" value="重置"/>
</form>
</body>
</html>