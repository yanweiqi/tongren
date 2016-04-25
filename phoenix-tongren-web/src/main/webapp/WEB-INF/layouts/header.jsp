<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="header" class="row">
	<div><h1>桐人<small>----首页</small></h1></div>
	<div class="pull-right">
	    <c:choose>
		   <c:when test = "${empty username}"> <a href="${ctx}/login">登录</a></c:when>
		   <c:when test = "${not empty username}">你好, ${username} <a href="${ctx}/logout">退出登录</a></c:when>
		</c:choose>  
	</div>
</div>