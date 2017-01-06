<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta charset="UTF-8">

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/css/materialize.min.css">
<link rel='stylesheet prefetch' href='https://fonts.googleapis.com/icon?family=Material+Icons'>
<link rel="stylesheet" href="/node_modules/jquery-colorbox/colorbox.css">
<link rel="stylesheet" href="/adminPage/adminPage.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" />

</head>

<body>


	<div class="navbar-fixed">
		<nav class="custom-nav">
			<div class="nav-wrapper">
				<a href="#!" class="brand-logo left">&nbsp;&nbsp;&nbsp;관리자 페이지</a>
			</div>
		</nav>
	</div>

	<div class="row">
		<div class="col s12">
			<ul class="tabs">
				<li class="tab col s3"><a class="${path=='pick'? 'active':''}" href="#pick">투표 관리</a></li>
				<li class="tab col s2"><a class="${path=='user'? 'active':''}" href="#user">사용자 관리</a></li>
				<li class="tab col s3"><a class="${path=='category'? 'active':''}" href="#category">카테 고리 관리</a></li>
			</ul>
		</div>
		<!--    User Info-->
		<div id="user" class="col s12">
			<div class="row">
				<div class="col s12">
					<div class="card material-table">
						<div class="table-header">
							<span class="table-title">투표 관리</span>
							<div class="actions">
								<a href="#" class="search-toggle waves-effect btn-flat nopadding">
									<i class="material-icons">search</i>
								</a>
							</div>
						</div>
						<table class="datatable">
							<thead>

								<tr>

									<th data-field="Email">이메일</th>
									<th data-field="Name">닉네임</th>
									<th data-field="Gender">성별</th>
									<th data-field="Age">연령대</th>
									<th data-field="Password">비밀번호</th>
								</tr>

							</thead>
							<tbody>
								<c:forEach var="user" items="${userList}">

									<tr id="getUser_${user.userNo}" style="cursor: pointer;">
										<td>${user.userEmail}</td>
										<td>${user.userName}</td>
										<td>${user.userGender=='male'?'남자':'여자'}</td>
										<td>${user.userAge.replace('s','대')}</td>
										<td>${user.userPassword}</td>
									</tr>

								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div id="pick" class="col s12">
			<!--   AdminPick     -->
			<div class="row">
				<div class="col s12">
					<div class="card material-table">
						<div class="table-header">
							<span class="table-title">투표 관리</span>
							<div class="actions">
								<a href="#" class="search-toggle waves-effect btn-flat nopadding">
									<i class="material-icons">search</i>
								</a>
							</div>
						</div>
						<table class="datatable">
							<thead>

								<tr>
									<th data-field="UserEmail">투표 등록자</th>
									<th data-field="Category">카테고리</th>
									<th data-field="Title">제목</th>
									<th data-field="VoteType">투표 타입</th>
									<th data-field="EndDate">종료일</th>
									<th data-field="PicnNum">투표 참여수 </th>
									<th data-field="Result">&nbsp;&nbsp;&nbsp;결과 보기</th>
								</tr>
							</thead>

							<tbody>
								<c:forEach var="vote" items="${voteList}">
									<tr>
										<td>${userEmailMapByUserNoMap.get(vote.userNo)}</td>
										<td>${vote.voteCategory}</td>
										<td>${vote.voteTitle}</td>
										<td>${vote.voteType}</td>
										<td>${vote.endDate}</td>
										<td>${totalCountByVoteNoMap.get(vote.voteNo)}</td>
										<td><div id="get_result_${vote.voteNo}" class="btn waves-effect waves-light btn-small custom-btn">result</div></td>
									</tr>
								</c:forEach>

							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>

		<!--Catrgory Info -->
		<div id="category" class="col s12">
			<div class="row">
				<div class="col  s12">
					<div class="card material-table">
						<div class="table-header">
							<span class="table-title">카테고리 관리</span>
							<div class="actions">
								<a href="#modal1" class="modal-trigger waves-effect btn-flat nopadding">
									<i class="material-icons">add_circle_outline</i>
								</a>
								<a href="#" class="search-toggle waves-effect btn-flat nopadding">
									<i class="material-icons">search</i>
								</a>
							</div>
						</div>
						<table class="datatable">
							<thead>
								<tr>

									<th data-field="#">카테고리 이름</th>
									<th data-field="#">이미지</th>

								</tr>
							</thead>
							<tbody>
								<c:forEach var="interest" items="${interestList}">
									<tr style="cursor: pointer;">
										<td>${interest.content}</td>
										<td><div class="thumb">
												<img src="/image/interest/thumbnail/${interest.interestPhoto}" />
											</div></td>

									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- Modal Trigger -->
	<!-- Modal Structure -->
	<div id="modal1" class="modal custom-modal">
		<div class="navbar-fixed">
			<nav class="custom-nav">
				<div class="nav-wrapper">
					<a href="#!" class="brand-logo left">&nbsp;&nbsp;카테고리 등록 </a>
				</div>
			</nav>
		</div>
		<div class="modal-content" id="catemodal">


			<form id="add_interest_form">
				<div class="preview img-wrapper"></div>
				<div class="file-upload-wrapper">
					<input type="file" name="interestImage" class="file-upload-native" accept="image/*" />
					<input type="text" disabled placeholder="upload" class="file-upload-text" />
				</div>


				<div class="row">
					<div class="input-field col offset-s2 s8">
						<input name="content" id="content" type="text" class="validate">
						<label for="icon_prefix">Category Name</label>
					</div>


					<div id="add_interest_btn" class="btn waves-effect waves-light col offset-s7 s4 custom-btn ">A D D</div>

				</div>
			</form>
		</div>



	</div>
	<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	<!-- <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>-->

	<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/js/materialize.min.js"></script>
	<script src='http://cdn.datatables.net/1.10.7/js/jquery.dataTables.min.js'></script>
	<script src="/node_modules/jquery-colorbox/jquery.colorbox-min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>
	<script src="/adminPage/adminPage.js"></script>
	<script type="text/javascript">
	
	$("div[id^=get_result]").on("click",function(){
			
		var voteNo = $(this).attr("id").replace("get_result_","");
		location.href="/vote/getResult/"+voteNo;
		
	});

		$("#add_interest_btn")
				.on(
						"click",
						function() {
							var form = new FormData($("#add_interest_form")[0]);

							swal(
									{
										title : "["+$("#content").val()
												+ "] 카테고리를 추가 하시겠습니까?",
										type : "info",
										confirmButtonColor : "#ED2553",
										showCancelButton : true,
										closeOnConfirm : false,
										showLoaderOnConfirm : true,
									},
									function(isConfirm) {
										if (isConfirm) {
											$
													.ajax({
														type : "post",
														url : "/user/addInterest",
														processData : false,
														contentType : false,
														data : form,
														success : function(data) {
															if (data.interestNo == 0) {
																swal({
																	title : "이미 해당 카데코리가 존재 합니다. ",
																	confirmButtonColor : "#ED2553"
																});
															} else {

																setTimeout(
																		function() {
																			swal(
																					{
																						title : data.content
																								+ " 카테고리가 추가되었습니다. ",
																						confirmButtonColor : "#ED2553",
																						imageUrl : "/image/interest/thumbnail/"
																								+ data.interestPhoto
																					},
																					function(
																							isConfirm) {
																						location.href = "/user/getAdminPageView/category";
																					});
																		}, 5000);

															}
														}
													});
										}
									});
						});
	</script>
</body>

</html>