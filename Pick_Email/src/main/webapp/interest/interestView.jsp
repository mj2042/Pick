<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/interest/interestView.css">
<link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
</head>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/css/materialize.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" />

<body>
	<div class="navbar-fixed">
		<nav class="custom-nav">
			<div class="nav-wrapper">
				<a href="#!" class="brand-logo left">&nbsp;&nbsp;&nbsp;Detail infomation</a>
			</div>
		</nav>
	</div> 

	<div id="container">
		<div class="row">
			
			<form id="detailInfo-form">
				<div class="input-field col s12">
						<input name="userName" id="icon_prefix" type="text" class="validate">
						<label for="icon_prefix">Nick Name</label>
				</div>
			
				<div class="input-field col s12">
					
					<select name="userAge">
						<option value="" disabled selected>Age</option>
						<option value="10s">10</option>
						<option value="20s">20</option>
						<option value="30s">30</option>
						<option value="40s">40</option>
						<option value="50s">50</option>
						<option value="60s">60~</option>
					</select>
					<label>Age</label>
				</div>
				<div class="input-field col s12">
					<select name="userGender">
						<option value="" disabled selected>Choose your gender</option>
						<option value="male">Male</option>
						<option value="female">Female</option>
					</select>
					<label>gender</label>
				</div>
				<div class="input-field col s12">
					<select name="interestList" multiple>
						<option value="" disabled>Choose your Interest</option>
						<c:forEach var="interest" items="${interestList}">
							<option value="${interest.interestNo}" data-icon="/image/interest/thumbnail/${interest.interestPhoto}" class="circle">${interest.content}</option>


						</c:forEach>
					</select>
					<label>Interest</label>
				</div>
			</form>
			<div class="col s6 offset-s6">
				<button class="btn waves-effect waves-light" id="register_btn">
					Register <i class="material-icons right">send</i>
				</button>
			</div>
		</div>
		<!---->
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/js/materialize.min.js"></script>

		<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>

		<script src='/interest/interestView.js'></script>


		<script type="text/javascript">
			
			$("#register_btn").on("click",function() {
				
							var nickName = $('input[name="userName"]').val();
							if(nickName==''){
								swal({
									title : "닉네임을 입력 해주세요.",
									confirmButtonColor : "#ED2553"
								});
								return;
							}

							
							 if ($('select[name="userAge"] option:selected')
										.val() == '') {
									swal({
										title : "연령대를 선택해주세요.",
										confirmButtonColor : "#ED2553"
									});
									return;
								}
								if ($('select[name="userGender"] option:selected')
										.val() == '') {
									swal({
										title : "성별을 선택해주세요.",
										confirmButtonColor : "#ED2553"
									});
									return;
								}
								if ($('select[name="interestList"] option:selected')
										.prevAll().size() < 3) {
									swal({
										title : "관심사를 3개이상 선택해주세요.",
										confirmButtonColor : "#ED2553"
									});
									return;
								}

							
							
							$.ajax({
								url : "/user/checkNickNameDuplication/"+nickName,
								type : 'GET',
						
								success : function(data) {
									if (data.isDuplicated == true) {
										swal({
											title : "이미 존재하는 닉네임입니다.",
											confirmButtonColor : "#ED2553"
										});
										$('input[name="userName"]').val(""); 
									
									} else{
										
										$("#detailInfo-form").attr("target", "_parent")
										.attr("action", "/user/addUser").attr(
												"method", "post").submit();
										
										
									}
								}
								
							});
							
						
							
							
							

							});
		</script>
	</div>
</body>

</html>