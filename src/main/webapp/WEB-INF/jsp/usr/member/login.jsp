<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="LOGIN"></c:set>
<%@ include file="../common/head.jspf"%>

<style>
/* 로그인 디자인 */
.login-box {
	width: 420px;
	margin-left: auto;
	margin-right: auto;
	white-space: nowrap;
	border: 1px solid rgba(255, 0, 0, 0.1);
	text-align: center;
	white-space: nowrap;
}

.login-box>thead tr {
	background: linear-gradient(to right, #536976, #292e49);
}

.login-box>thead th {
	color: white;
}

.login-box>tbody th {
	font-size: 14px;
}

.login-box th, td {
	border: 1px solid rgba(255, 0, 0, 0.1);
}

</style>

<section class=" text-xl">
	<div class="mt-36">
		<form action="../member/doLogin" method="POST">
			<input type="hidden" name="afterLoginUri" value="${param.afterLoginUri }" />
			<table class="login-box" border="1">
				<thead>
					<tr>
						<th colspan="2">LOGIN</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th>아이디</th>
						<td>
							<input class="input input-bordered input-secondary w-full max-w-xs" autocomplete="off" type="text"
								placeholder="아이디를 입력해주세요" name="loginId"
							/>
						</td>
					</tr>
					<tr>
						<th>비밀번호</th>
						<td>
							<input class="input input-bordered input-secondary w-full max-w-xs" autocomplete="off" type="text"
								placeholder="비밀번호를 입력해주세요" name="loginPw"
							/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<input class="btn btn-outline btn-info" type="submit" value="로그인" />
							
						</td>
					</tr>
				</tbody>
			</table>
			<div style="text-align:center; font-size:13px; padding-left:370px">
			<a href="join">회원가입</a>
			</div>
		</form>
		<!-- 		<div class="btns"> -->
		<!-- 			<button class="btn btn-outline" type="button" onclick="history.back();">뒤로가기</button> -->
		<!-- 		</div> -->
	</div>
</section>



<%@ include file="../common/foot.jspf"%>