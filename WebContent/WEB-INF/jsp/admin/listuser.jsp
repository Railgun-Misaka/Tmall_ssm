<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<script>
</script>
<title>用户管理</title>


<div class="workingArea">
	<h1 class="label label-info" >用户管理</h1>

	<br>
	<br>
	<div class="listDataTableDiv">
		<table class="table table-striped table-bordered table-hover  table-condensed">
			<tr  class="success">
				<th>id</th>
				<th>用户名</th>
			</tr>
			<c:forEach items="${users}" var="user">
				<tr>
					<td>${user.id}</td>
					<td>${user.name}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<div class="pageDiv">
		<%@include file="../include/admin/adminPage.jsp" %>
	</div>
</div>

<%@include file="../include/admin/adminFooter.jsp"%>