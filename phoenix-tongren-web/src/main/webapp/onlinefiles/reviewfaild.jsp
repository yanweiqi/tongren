<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div>
<%
String fileName=request.getParameter("fileName");
if(fileName!=null){
	fileName=URLDecoder.decode(fileName, "utf-8");
}
%>
<div>预览
<span style="color: red"><%=fileName %></span>
失败！</div>
</div>
</body>
</html>