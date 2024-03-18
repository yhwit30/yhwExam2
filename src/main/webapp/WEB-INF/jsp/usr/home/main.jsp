<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MAIN"></c:set>
<%@ include file="../common/head.jspf"%>



<div class="page mt-10  mx-auto">
	
	<div class="main-page">
		<a class="btn btn-lg btn-block btn-outline" href="/usr/article/list">게시판</a>
	</div>

</div>


<style type="text/css">
.mainSearch {
	text-align: center;
}

.page {
	display: flex;
/* 	border: 5px solid red; */
	height: 500px;
	width: 1000px;
	justify-content: space-around;
	
}


.main-page {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 250px;
	height: 400px;
}


</style>



<!-- 페이지 하단 -->
<%@ include file="../common/foot.jspf"%>