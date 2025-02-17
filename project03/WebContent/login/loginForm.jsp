<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<!-- 카카오 -->
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>

<!-- 네이버 -->
<script type="text/javascript" src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.0.js" charset="UTF-8"></script>

<!-- 구글 -->
<meta name="google-signin-scope" content="profile email">
<script src="https://apis.google.com/js/platform.js" async defer></script>
<meta name="google-signin-client_id" content="320557608369-9srdo4fso2icfd704v837fql0jnh10cl.apps.googleusercontent.com">


<body>
<header>
	<%@ include file="../main/header.jsp" %>
</header>

<%
	boardType = request.getParameter("boardType");
	if(session.getAttribute("sId") != null || session.getAttribute("sAdmin") != null){%>
		<script>
			alert("이미 로그인 한 상태입니다.");
			history.go(-1);
		</script>
	<%}


%>
<script>
	var boardType = <%=boardType%>;
</script>
<!-- 일반 유저의 경우 loginForm 을 통해 입력받아서 로그인한다  -->
<form action="loginPro.jsp?boardType=<%=boardType%>" method="post">
	
	<table>
		<tr>
			<td>아이디 : <input type="text" name="id"/></td>
		</tr>
		<tr>
			<td>비밀번호 : <input type="password" name="pw"/></td>
		</tr>
		<tr>
			<td>로그인 유지 : <input type="checkbox" name="auto" value="1"/></td>
		</tr>
		<tr>
			<td><input type="submit" value="로그인"/>
				<input type="button" value="돌아가기" onclick="location.href='../main/main.jsp'"/></td>
		</tr>
	</table>
</form>

	<!-- 네이버 로그인 -->
	<div id="naverIdLogin"></div>
	
	<script type="text/javascript">
		var naverLogin = new naver.LoginWithNaverId(
			{
				clientId: "MOf0l_qoj5M7p4rLoe4B",
				callbackUrl: "http://192.168.0.18:8080/project01/login/naverLogin.jsp?boardType="+boardType,
				// OAUTH 정보를 받을 콜백 URL을 지정해준다.
				isPopup: false, /* 팝업을 통한 연동처리 여부 */
				loginButton: {color: "green", type: 3, height: 48} /* 로그인 버튼의 타입을 지정 */
			}
		);
		
		/* 설정정보를 초기화하고 연동을 준비 */
		naverLogin.init();
	</script>
	<!-- 카카오 로그인 -->
	<a id="kakao-login-btn"></a>
	<a href="http://developers.kakao.com/logout"></a>
	<script type='text/javascript'>
	  //<![CDATA[
	    // 사용할 앱의 JavaScript 키를 설정해 주세요.
	    Kakao.init('b607f149ec3e12abaff481e0ec56f3bb');
	    // 카카오 로그인 버튼을 생성합니다.
	    Kakao.Auth.createLoginButton({
			container: '#kakao-login-btn',
			success: function(authObj) {
				Kakao.API.request({
					url: '/v1/user/me',
					success: function(res) {
						console.log(res);
						var userID = "kakao_" + res.id;      //유저의 카카오톡 고유 id
						var loginURL = "http://192.168.0.18:8080/project01/login/kakaoLoginPro.jsp?id="+encodeURI(userID)
								+"&boardType="+boardType;
						// OAUTH로 받은 정보를 해당 URL로 전송한다.
						window.location.replace(loginURL);
					},
					fail: function(error) {
						alert(JSON.stringify(error));
					}
				});
			},
			fail: function(err) {
				alert(JSON.stringify(err));
			}
		});
	  //]]>
	</script>
	
	<!-- 구글 -->
	
	<div id="my-signin2"></div>
		<script>
	    function onSuccess(googleUser){
	    	console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
	    	var profile = googleUser.getBasicProfile();
			console.log("ID: " + profile.getId()); // Don't send this directly to your server!
			var googleId = "google_" + profile.getId();
			var googleEmail = profile.getEmail();
	        var loginURL = "http://192.168.0.18:8080/project01/login/googleLoginPro.jsp?id="+encodeURI(googleId)+"&boardType="+boardType;
			// OAUTH로 받은 정보를 해당 URL로 전송한다.
			window.location.replace(loginURL);
	    }
	    function onFailure(error){
			console.log(error);
		}
	    function renderButton() {
			gapi.signin2.render('my-signin2', {
	        'scope': 'profile email',
	        'width': 222,
	        'height': 48,
	        'longtitle': true,
	        'theme': 'dark',
	        'onsuccess': onSuccess,
	        'onfailure': onFailure
	      });
	    }
	  </script>
	  <script src="https://apis.google.com/js/platform.js?onload=renderButton" async defer></script>
	  

</body>
</html>