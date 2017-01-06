<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link rel="stylesheet" href="/node_modules/jquery-colorbox/colorbox.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/css/materialize.min.css">
<link rel="stylesheet" href="/loginAndSignUp/loginAndSignUpView.css">


</head>

<body>
	<!--  Hidden Data-->

	<input type="hidden" id="fromGetVote" value="${fromGetVote}">
	<div class="section"></div>



	<div class="container">
		<div class="row">
			<div class="col offset-s2 s8 center">
				<img class="responsive-img" style="width: 100px;" src="/image/main/pick160.png" />

				<h5 class="indigo-text">Please, login into your account</h5>
				<div class="section"></div>
			</div>
			<div class="col offset-s4 s4 z-depth-1 grey lighten-4" style="padding: 30px 30px 0px 30px; border: 1px solid #EEE;"">



				<form id="login_form" class="col s12" method="post">
					<div class='row'>
						<div class='col s12'></div>
					</div>

					<div class='row'>
						<div class='input-field col s12'>
							<input class='validate' type='email' name='userEmail' id='userEmail' />
							<label for='email'>Enter your email</label>
						</div>
					</div>

					<div class='row'>
						<div class='input-field col s12'>
							<input class='validate' type='password' name='userPassword' id='userPassword' />
							<label for='password'>Enter your password</label>
						</div>

					</div>

					<br />

					<div class='row'>
						<div id="login_btn" class='col s12 btn btn-large waves-effect indigo'>Login</div>
					</div>
					<div class='row'>
						<div id="fb_login_btn" class='col s12 btn btn-large waves-effect indigo'>Facebook login</div>
					</div>


				</form>

				<form id="register_form" class="col s12" method="post" style="display: none">
					<div class='row'>
						<div class='col s12'></div>
					</div>

					<div class='row'>
						<div class='input-field col s12'>
							<input class='validate' type='email' name='userEmail' id='registerUserEmail' />
							<label for='email'>Enter your email</label>
						</div>
					</div>

					<div class='row'>
						<div class='input-field col s12'>
							<input class='validate' type='password' name='userPassword' id='registerUserPassword' />
							<label for='password'>Enter your password</label>
						</div>
					</div>
					<div class='row'>
						<div class='input-field col s12'>
							<input class='validate' type='password' name='userPasswordConfirm' id='registerUserPasswordConfirm' />
							<label for='password'>Enter your password Confirm</label>
						</div>
					</div>

					<br />

					<div class='row'>
						<div id="register_btn" class='col s12 btn btn-large waves-effect indigo'>Register</div>
					</div>

				</form>
			</div>
			<div class="col offset-s4 s4 center">
				<br />
				<div id="change_register_form" class="change_btn" style="cursor: pointer;">Create account</div>
				<div id="change_login_form" class="change_btn" style="cursor: pointer; display: none;">login</div>
			</div>

		</div>
	</div>




	<div class="section"></div>
	<div class="section"></div>



	<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.1/jquery.min.js"></script>
	<script src="/node_modules/jquery-colorbox/jquery.colorbox-min.js"></script>
	<script src="/loginAndSignUp/loginAndSignUpView.js"></script>

	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js"></script>

	<!-- 페이스북 로그인  -->
	<script type="text/javascript">
	
	window.fbAsyncInit = function() {
	    FB.init({
	      appId      : '224865167968997',
	      cookie     : true,  
	      xfbml      : true,  
	      version    : 'v2.8' 
	    });
	    
	
	  
	};
	(function(d, s, id){
	     var js, fjs = d.getElementsByTagName(s)[0];
	     if (d.getElementById(id)) {return;}
	     js = d.createElement(s); js.id = id;
	     js.src = "//connect.facebook.net/ko_KR/sdk.js";
	     fjs.parentNode.insertBefore(js, fjs);
	   }(document, 'script', 'facebook-jssdk'));


$("#fb_login_btn").on("click",function(){
		
		FB.login(function(response) {//로그인 대화 상자 팝업( Facebook 호출을 요청하여 로그인 상태를 가져오고 결과를 통해 콜백 함수를 호출한다.)
			
	    	  if (response.status === 'connected') {  //사용자가 Facebook에 로그인하고 앱에 로그인(계정에 앱 추가) 했다. 
	    			
	    		 	 FB.logout();  //로그아웃 시키고 진행 
	    			 FB.api('/me',{fields: 'email'}, function(user) {//데이터를 얻기 위해서 또는 유저에 대한 도움(?)을 위한 조치를 취하기 위해서 Facebook Graph API를 호출
						var user = {"userEmail" : user.email,
	  								"userPassword":12341234,//비밀번호는 임시로  일단 임의의 숫자  
	  								"userPhoto" :'fb_profile_image//graph.facebook.com/'+response.authResponse.userID+'/picture?width=80&height=80'}; 
	  					
						$.ajax({
	    					url : "/user/checkDuplicationForFaceBook",
	    					type : 'POST',
	    					data : user,
	    					success : function(data) {	
	    						if (data.isDuplicated == true) { //계정이 존재 할 경우 main  
	    							location.href="/user/main";
							
								} else { //계정이 존재 하지 않을  경우 상세 정보를 입력 받는다. 
									swal({
										title : "해당 계정이 가입이 되어있지 않습니다. 가입하시겠습니까?",
										confirmButtonColor : "#ED2553"
									},function(isConfirm) {
										if (isConfirm) {
											$.colorbox({
												iframe : true,
												scrolling : false,
												innerWidth : '280',
												innerHeight : '450',
												href : "/user/getDetailInfomationSelectView",
												onClosed : function() {
												}
											});
										}
									});
								
								}
							}
	    				});
	    			
	    		 });  
	    	  } else if (response.status === 'not_authorized') {//사용자가 Facebook에 로그인 했지만 앱에 로그인 하지 않았다.
	    			 //alert("not_authorized");
	    	  } else {     //사용자가 Facebook에 로그인 하지 않았으므로 앱에 로그인했는지 알수 없다.또는 FB.logout()이 호출 되었다.  (unknown)
	    	   		//alert("else");
	    	  }
	   	},{//이메일 권한 요청  (반드시 필요)
		      scope: 'email',
		      return_scopes: true,
		      auth_type: 'rerequest'
		 });
	});

	</script>






















	<script type="text/javascript">
		
		
		if($("#fromGetVote").val()=='true'){
			swal({
				title : "로그인 후 투표 참여가 가능합니다. 간단해요. 계정이 없으시면  가입해주세요.",
				confirmButtonColor : "#ED2553"
			});
		}
	
	
		 $("#change_login_form").on("click", function() {
			$(this).css("display", "none");
			$(this).removeAttr("disabled");
			$("#register_form").css("display", "none");
			$("#register_form").attr("disabled", "disabled");

			$("#login_form").css("display", "inline");
			$("#change_register_form").css("display", "inline");
		});

		$("#change_register_form").on("click", function() {
			$(this).css("display", "none");
			$(this).removeAttr("disabled");
			$("#login_form").css("display", "none");
			$("#login_form").attr("disabled", "disabled");

			$("#register_form").css("display", "inline");
			$("#change_login_form").css("display", "inline");
		}); 



			$("#login_btn").on("click", function() {
				var user = {
					"userEmail" : $("#userEmail").val(),
					"userPassword" : $("#userPassword").val()
				};
				$.ajax({
					url : "/user/login",
					type : 'POST',
					accept : "application/json",
					contentType : "application/json; charset=utf-8",
					data : JSON.stringify(user),
					dataType : "json",
					success : function(data) {	
						var loginCheck = data.loginCheck;
						if (loginCheck=='success') {
							location.href = "/user/loginSuccess";

						} else if (loginCheck=='passwordError') {
							swal({
								title : "비밀번호가 일치하지 않습니다.",
								confirmButtonColor : "#ED2553"
							});
							$("#userPassword").val("");

						} else {
							swal({
								title : "해당 이메일이 존재하지 않습니다.",
								confirmButtonColor : "#ED2553"
							
							});
							$("#userEmail").val("");
							$("#userPassword").val("");

						}

					},
					error : function(jqXHR, textStatus, errorThrown) {
						alert("Error");
					}
				});

			});

			/* 회원 가입 버튼 */
			$("#register_btn").on("click",function() {
								if($("#registerUserEmail").val()==''){
									swal({
										title : "이메일을 입력 하세요.",
										confirmButtonColor : "#ED2553"
									});
									return;
								}
								
								if($("#registerUserPassword").val()==''){
									swal({
										title : "패스워드를 입력 하세요.",
										confirmButtonColor : "#ED2553"
									});
									return;
								}
								
								
								if($("#registerUserPasswordConfirm").val()==''){
									swal({
										title : "패스워드(확인)을 입력 하세요.",
										confirmButtonColor : "#ED2553"
									});
									return;
								}
									
								if ($("#registerUserPassword").val() != $("#registerUserPasswordConfirm").val()) {
									swal({
										title : "비밀번호가 일치하지 않습니다.",
										confirmButtonColor : "#ED2553"
									});
									return;
								}

								var user = {
									"userEmail" : $("#registerUserEmail").val(),
									"userPassword" : $("#registerUserPassword").val()
								};

								$.ajax({
											url : "/user/checkDuplication",
											type : 'POST',
											accept : "application/json",
											contentType : "application/json; charset=utf-8",
											data : JSON.stringify(user),
											dataType : "json",
											success : function(data) {
												if (data.isDuplicated == true) {
													swal({
														title : "이미 존재하는 이메일입니다.",
														confirmButtonColor : "#ED2553"
													});
													$("#registerUserEmail")
															.val("");
												} else {
													/*중복이 아닐 경우 상세 정보선택 창 팝업 */
													$.colorbox({
																iframe : true,
																scrolling : false,
																innerWidth : '280',
																innerHeight : '450',
																href : "/user/getDetailInfomationSelectView",
																onClosed : function() {

																}
															});

												}
											},
											error : function(jqXHR, textStatus,
													errorThrown) {
												swal("Error");
											}

										});
							});
			
			
			
	</script>

</body>

</html>