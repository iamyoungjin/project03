<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width"/>
<!-- 카카오  -->
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
<!-- 네이버 -->
<script type="text/javascript" src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.0.js" charset="utf-8"></script>
<!-- 구글 -->
<meta name="google-signin-scope" content="profile email">
<script src="https://apis.google.com/js/platform.js" async defer></script>
<meta name="google-signin-client_id" content="320557608369-9srdo4fso2icfd704v837fql0jnh10cl.apps.googleusercontent.com">

</head>
<body>
<header>
	<%@ include file="../main/header.jsp" %>
</header>

<%
	// 세션값을 통해 로그인 체크
	sId = (String)session.getAttribute("sId"); 
	sAdmin = (String)session.getAttribute("sAdmin");
	if(sId!=null || sAdmin!=null){
		response.sendRedirect("../main/main.jsp");
	}else{
	
%>

	<input type="button" value="회원가입" onclick="window.location.href='signUpForm.jsp'">
	<input type="button" value="메인으로" onclick="window.location.href='../main/main.jsp'"><br/>
	
	<!--  카카오 로그인 API -->
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
						var userID = "kakao_" + res.id;
						// 유저 고유 id의 앞에 kakao_를 붙여서 카카오sns 유저로 구분
						var loginURL = "http://192.168.0.18:8080/project01/sign/kakaoSignUpPro.jsp?id="+encodeURI(userID);
						window.location.replace(loginURL);
						// URL에 변수값을 더해 전송한다.
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
	
	<!-- 네이버 -->
	<div id="naverIdLogin"></div>
	
	<script type="text/javascript">
		var naverLogin = new naver.LoginWithNaverId(
			{
				// oauth 정보를 callback 페이지에서 받게 한다.
				clientId: "MOf0l_qoj5M7p4rLoe4B",
				callbackUrl: "http://192.168.0.18:8080/project01/sign/naverSignUp.jsp",
				isPopup: false, /* 팝업을 통한 연동처리 여부 */
				loginButton: {color: "green", type: 3, height: 48} /* 로그인 버튼의 타입을 지정 */
			}
		);
		
		/* 설정정보를 초기화하고 연동을 준비 */
		naverLogin.init();
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
			
			// oauth에서 받아온 값을 pro 페이지로 보낸다
			var loginURL = "http://192.168.0.18:8080/project01/sign/googleSignUpPro.jsp?id="+encodeURI(googleId)+"&email="+encodeURI(googleEmail);
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
	<%} %>
</body>
</html>