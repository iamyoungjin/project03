<%@page import="test.web.project03.MemberDTO"%>
<%@page import="test.web.project03.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<script>
	function writeSave(){
		if(!document.writeForm.subject.value){
		  alert("제목을 입력하십시요.");
		  return false;
		}
		if(document.writeForm.content.value.length == 0){
		  alert("내용을 입력하십시요.");
		  document.writeform.content.focus();
		  return false;
		}
	    
	 }   
</script>
</head>
<header>
	<%@ include file="../main/header.jsp" %>
</header>

<%
	sId = (String)session.getAttribute("sId");
	sAdmin = (String)session.getAttribute("sAdmin");
	if(session.getAttribute("sId") == null && session.getAttribute("sAdmin") == null){%>
		<script>
			alert("글쓰기는 로그인 후 사용할 수 있습니다.");
			history.go(-1);
		</script>
	<%}else{
		//  boardnum, ref, re_step, re_level을 선언+초기화
		int boardnum=0,ref=1,re_step=0,re_level=0;
		try{
			// 만일, 해당 글 작성이 content에서 답글로 넘어왔을시, 해당 글에서 boardnum, ref, re_step, re_level을 전부 가져온다
			if(request.getParameter("boardnum")!= null){
				boardnum = Integer.parseInt(request.getParameter("boardnum"));
				ref = Integer.parseInt(request.getParameter("ref"));
				re_step = Integer.parseInt(request.getParameter("re_step"));
				re_level = Integer.parseInt(request.getParameter("re_level"));
			}
			int category = 0;
			if(session.getAttribute("sAdmin")!=null){ category = 2; }
			else{category = 1;} 
			
			MemberDAO dao = MemberDAO.getInstance();
			if(session.getAttribute("sAdmin")!=null){sId = sAdmin;}
			MemberDTO dto = dao.getMember(sId);
	%>
	<body>
	<center>글쓰기
	<!-- 파일 업로드를 위해 form의 인코딩 타입을 multipart/form-data 로 인코딩 -->
	<form name="writeForm" action="writePro.jsp" method="post" onsubmit="return writeSave()" enctype="multipart/form-data">
		<input type="hidden" name="boardnum" value="<%=boardnum %>"  />
		<input type="hidden" name="ref" value="<%=ref%>" />
		<input type="hidden" name="re_step" value="<%=re_step%>" />
		<input type="hidden" name="re_level" value="<%=re_level%>" />
		<input type="hidden" name="name" value="<%=dto.getName() %>" />
		<input type="hidden" name="id" value="<%=dto.getId() %>" />
		<input type="hidden" name="pw" value="<%=dto.getPw() %>" />
		<input type="hidden" name="category" value="<%=category %>" />
		<table border="1">
			<tr>
				<td>작성자 <input type="text" name="name" value="<%=dto.getName()%>" readonly/> </td>
			</tr>
			<tr>
				<td>제목<%
				// 답글이 아닐 경우 그냥 작성, 답글일 경우 기본값에 RE:를 달아준다.
				if(request.getParameter("boardnum")==null){%>
					<input type ="text" name="subject"/>
				<%}else{ %>
					<input type="text" name="subject" value="RE:"/>
				<%}%>
				</td>
			</tr>
			<tr>
				<td>내용 <textarea rows="13" cols="40" name="content"></textarea></td>
			</tr>
			<tr>
				<td>사진 업로드 : <input type="file" name="save"/><br/> </td>
			</tr>
			<tr>
				<td>
				<input type="submit" value="글쓰기"/>
				<input type="reset" value="다시작성"/>
				<input type="button" value="목록으로" onclick="window.location.href='boardList.jsp'"/>
				</td>
			</tr>
		</table>
		<%}catch(Exception e){
			e.printStackTrace();
		}%>
		
	</form>
	<%}
%>
<footer>
	<%@ include file="../main/footer.jsp" %>
</footer>
</body>
</html>