<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>

<head>
<meta charset="UTF-8">
<title>Account</title>
<link href='http://fonts.googleapis.com/css?family=Titillium+Web:400,300,600' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/css/materialize.min.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link rel="stylesheet" href="/account/accountView.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" />

</head>

<body>



	<div class="form">
		<div class="navbar-fixed">
			<nav class="custom-nav">
				<div class="nav-wrapper">
					<a href="#!" class="brand-logo left">&nbsp;&nbsp;&nbsp;내 정보 보기</a>
				</div>
			</nav>
		</div>

		<div class="row">
			<c:if test="${fromChat==true}">
				<div id="goToChat_btn" class="col s4 center btn">채팅방 돌아가기</div>
			</c:if>
			<form id="update_form" class="col s12" enctype="multipart/form-data">
				<div class="preview img-wrapper">
					<c:choose>
						<c:when test="${fn:startsWith(user.userPhoto, 'fb_profile_image')}">
							<img class="get_preview" src="${fn:replace(user.userPhoto,'fb_profile_image', '')}">
						</c:when>
						<c:otherwise>
							<img class="get_preview" src="/image/profile/thumbnail/${empty user.userPhoto?'defaultProfileImage.jpg':user.userPhoto}">
						</c:otherwise>
					</c:choose>
				</div>
				<div class="file-upload-wrapper">
					<input type="file" name="profileImage" class="file-upload-native" accept=".gif,.jpeg,.jpg,.png" />
					<input type="text" disabled placeholder="upload" class="file-upload-text" />
				</div>
				<div class="row">
					<div class="input-field col s6">
						<input name="userName" id="icon_prefix" type="text" class="validate" value="${user.userName}">
						<label for="icon_prefix">Name</label>
					</div>
					<div class="input-field col s6">
						<input name="userEmail" disabled value="${user.userEmail}" id="disabled" type="text" class="validate">
						<label for="disabled">E mail</label>
					</div>
					<div class="input-field col s12">
						<select name="formInterestList" multiple>
							<option value="" disabled selected>Choose your Interest</option>
							<c:forEach var="interest" items="${interestList}">
								<option value="${interest.interestNo}" data-icon="/image/interest/thumbnail/${interest.interestPhoto}" class="circle" ${user.interestList.contains(interest)?'selected':''}>${interest.content}</option>
							</c:forEach>
						</select> <label>Interest</label>
					</div>
				</div>

				<div class="row">

					<div class="input-field col s6">
						<select name="userGender">
							<option value="" disabled selected>Gender</option>
							<option value="male" ${user.userGender=='male' ?'selected':''}>male</option>
							<option value="female" ${user.userGender=='female' ? 'selected':''}>female</option>
						</select> <label>Gender</label>
					</div>
					<div class="input-field col s6">
						<select name="userAge">
							<option value="" disabled selected>Age</option>
							<option value="10s" ${user.userAge=='10s' ? 'selected':''}>10대</option>
							<option value="20s" ${user.userAge=='20s' ? 'selected':''}>20대</option>
							<option value="30s" ${user.userAge=='30s' ? 'selected':''}>30대</option>
							<option value="40s" ${user.userAge=='40s' ? 'selected':''}>40대</option>
							<option value="50s" ${user.userAge=='50s' ? 'selected':''}>50대</option>
							<option value="60s" ${user.userAge=='60s' ? 'selected':''}>60대 이상</option>
						</select> <label>Age</label>
					</div>

					<div class="col s12"></div>
					<div id="save_btn" class="btn waves-effect waves-light col offset-s6 s6">
						S A V E <i class="material-icons right">send</i>
					</div>
				</div>



			</form>
		</div>
	</div>
	<!--end of content-->
	<!--start of javascript-->
	<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

	<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/js/materialize.min.js"></script>
	<script src="/account/accountView.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>

	<script type="text/javascript">
	/*뒤로 가기 */
	
	$("#goToChat_btn").on("click",function(){
		location.href="/chat/chat";
	});
	
		
		$("#save_btn").on(
				"click",
				function() {
					if ($('select[name="formInterestList"] option:selected')
							.prevAll().size() < 3) {
						swal({
							title : "관심사를 3개이상 선택해주세요.",
							confirmButtonColor : "#ED2553"
						});
						return;
					}
					var form = new FormData($("#update_form")[0]);
					swal({
						title : "회원 정보를 수정 하시겠습니까?",
						type : "info",
						confirmButtonColor : "#ED2553",
						showCancelButton : true,
						closeOnConfirm : false,
						showLoaderOnConfirm : true,
					}, function(isConfirm) {
						if (isConfirm) {
							$.ajax({
								type : "post",
								url : "/user/updateUser",
								processData : false,/*data 파라미터로 전달된 데이터를 jQuery 내부적으로 query string 으로 만드는데, 파일 전송의 경우 이를 하지 않아야 하고 이를 설정하는 것이 processData: false 이다.*/
								contentType : false,/*contentType 은 default 값이 "application/x-www-form-urlencoded; charset=UTF-8" 인데, "multipart/form-data" 로 전송이 되게 false 로 넣어준다. */
								data : form,
								success : function(data) {
									setTimeout(function() {
										swal({
											title : data.userEmail
													+ "님 정보가 수정되었습니다.",
											confirmButtonColor : "#ED2553",
											showLoaderOnConfirm : true
										},function(isConfirm){
											parent.$.colorbox.close();
											parent.location.href = "/user/main";
										});
									}, 3000);
									
								}
							});
						}
					});
				});
	</script>

</body>

</html>