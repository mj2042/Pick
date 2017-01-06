<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/filter/filter.css">

<link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/css/materialize.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" />

</head>
<body>
	<div class="navbar-fixed">
		<nav class="custom-nav">
			<div class="nav-wrapper">
				<a href="#!" class="brand-logo left">&nbsp;&nbsp;&nbsp;필터링</a>
			</div>
		</nav>
	</div>

	<div id="container">
		<div class="row">
			<form id="filter-form">
				<div class="row">
					<div class="col s12 emp1"></div>
					<div class="col s12">Gender</div>
					<div class="col s12">


						<input type="checkbox" name="gender" class="filled-in" id="male" />
						<label for="male">Male</label>

						<input type="checkbox" name="gender" class="filled-in" id="female" />
						<label for="female">Female</label>
					</div>

					<div class="col s12 emp1"></div>
					<div class="col s12">Age</div>
					<div class="col s12">


						<input type="checkbox" name="age" class="filled-in" id="one" />
						<label for="one">10대</label>

						<input type="checkbox" name="age" class="filled-in" id="two" />
						<label for="two">20대</label>

						<input type="checkbox" name="age" class="filled-in" id="three" />
						<label for="three">30대</label>

						<input type="checkbox" name="age" class="filled-in" id="four" />
						<label for="four">40대</label>

						<input type="checkbox" name="age" class="filled-in" id="five" />
						<label for="five">50대</label>

						<input type="checkbox" name="age" class="filled-in" id="six" />
						<label for="six">60대</label>
						<div id='hidden_authority'>
						<input id="hidden_age_one" type="hidden" name="one" value="false" />
						<input id="hidden_age_two" type="hidden" name="two" value="false" />
						<input id="hidden_age_three" type="hidden" name="three" value="false" />
						<input id="hidden_age_four" type="hidden" name="four" value="false" />
						<input id="hidden_age_five" type="hidden" name="five" value="false" />
						<input id="hidden_age_six" type="hidden" name="six" value="false" />
						<input id="hidden_gender_male" type="hidden" name="male" value="false" />
						<input id="hidden_gender_female" type="hidden" name="female" value="false" />
					</div>

					</div>


					<div class="col s12 emp1"></div>
					<div class="input-field col s12">
						<select name="interestNoList" multiple>
							<option value="" disabled>Category</option>
							<c:forEach var="interest" items="${interestList}">
								<option value="${interest.interestNo}" data-icon="/image/interest/thumbnail/${interest.interestPhoto}" class="circle">${interest.content}</option>
							</c:forEach>
						</select> <label>Category</label>


					</div>
					<div class="col s6 offset-s6">
						<div class="btn waves-effect waves-light" id="filter_btn">Filtering</div>
					</div>

				</div>
				
			</form>

		</div>
		<!---->
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/js/materialize.min.js"></script>

		<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>
		
		<script src='/filter/filter.js'></script>


		<script type="text/javascript">
		
		
		$("#filter_btn").on("click",function(){
			/* 아무것도 선택하지 않았을 경우 권한 선택하지 않은것으로 간주함 */
	    	var ageCount =  $("input[name='age']:checkbox:checked").length;
	    	if(ageCount==0){
				$("#hidden_authority > input[id^=hidden_age]").val("true");
			}else{
				$("input:checkbox:checked").each(function(){
					$("div>input[id^='hidden_age_"+$(this).attr("id")+"']").val("true");
				});
			}
	    	
	    	var genderCount= $("input[name='gender']:checkbox:checked").length;
	    	if(genderCount==0){
				$("#hidden_authority > input[id^=hidden_gender]").val("true");
			}else{
				$("input:checkbox:checked").each(function(){
					$("div>input[id^='hidden_gender_"+$(this).attr("id")+"']").val("true");
				});
			}
	    	
	    	
	    	$("#filter-form").attr("method","post").attr("action","/vote/filter").attr("target","_parent").submit();
	    	
		});
		
		
		</script>
	</div>
</body>

</html>