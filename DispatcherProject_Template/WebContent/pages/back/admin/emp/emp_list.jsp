<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<%
	String basePath = request.getScheme() + "://" + 
		request.getServerName() + ":" + request.getServerPort() + 
		request.getContextPath() + "/" ;
%>
<base href="<%=basePath%>"/>
<title>商品管理</title>
<meta name="viewport" content="width=device-width,initial-scale=1">
<script type="text/javascript" src="jquery/jquery.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css">
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="h1"><strong><span class="glyphicon glyphicon-th-list"></span>&nbsp;商品信息列表</strong></div>
		</div>
		<div class="row">
			<jsp:include page="/pages/plugins/split_page_search_plugin.jsp"/>
			<table class="table table-striped table-bordered table-hover">
				<tr>
					<td>雇员编号</td>
					<td>雇员姓名</td>
				</tr>
				<c:forEach items="${allEmps}" var="emp">
				<tr>
					<td>${emp.empno}</td>
					<td>${emp.ename}</td> 
				</tr> 
				</c:forEach>
			</table>
			<jsp:include page="/pages/plugins/split_page_bar_plugin.jsp"/>
		</div>
	</div>
</body>
</html>