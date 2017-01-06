<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>

<head>
<meta charset="UTF-8">

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css">
<link rel='stylesheet prefetch' href='https://fonts.googleapis.com/css?family=Open+Sans:300,400,700'>
<link rel="stylesheet" href="/result/resultOne.css">
<!-- Fuentes de Google -->
<link href='http://fonts.googleapis.com/css?family=Roboto:400,700' rel='stylesheet' type='text/css'>
<!-- Iconos -->
<link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/css/materialize.min.css">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<body>

		<c:forEach items="${choiceList}" var="choice">
			<input type="hidden" id="getText_${choice.choiceNo}" value="${choice.content}">
			<input type="hidden" id="getPhoto_${choice.choiceNo}" value="${choice.photo}">
		</c:forEach>
	
	
	<div class="navbar-fixed">
		<nav class="custom-nav">
			<div class="nav-wrapper"> 
				<a href="#!" class="brand-logo left">&nbsp;&nbsp;&nbsp;투표 결과</a>
			</div>
		</nav>
	</div>



	<div id="container">
		<div class="row">

			<div class="col s12">
				<div class="row">
					<br />

					<div class="col offset-s2 s8 center">

						<div class="card-panel">

							<div id="voteTitle">${vote.voteTitle}</div>
							<br />
							<div id="voteContent" id="voteContent">${vote.voteContent}</div>
						</div>
					</div>
					<div class="col s12">
						<div id='dashboard'></div>
					</div>
				</div>

			</div>
			<div class="col offset-s2 s8">
				<div class="detailBox">
					<div class="titleBox">
						<label>너의 의견을 달아봐 </label>
					</div>
					<div class="actionBox">
						<ul id="commentList" class="commentList">
							<c:forEach var="comment" items="${commentList}">
								<li>
									<div class="commenterImage">
										<c:choose>
											<c:when test="${fn:startsWith(userPhotoByCommentNoMap.get(comment.commentNo), 'fb_profile_image')}">
												<img  src="${fn:replace(userPhotoByCommentNoMap.get(comment.commentNo),'fb_profile_image', '')}">
											</c:when>
											<c:otherwise>
												<img src="/image/profile/thumbnail/${userPhotoByCommentNoMap.get(comment.commentNo)}" />
											</c:otherwise>
										</c:choose>
									</div>
									<div class="commentText">
										<p class="">${comment.commentContent}</p>
										<span class="date sub-text">${comment.regDate}</span>

									</div> <c:if test="${comment.userNo==user.userNo}">
										<div id="delete_comment_${comment.commentNo}" class="delete_btn_custom">
											<i class="material-icons">remove_circle_outline</i>
										</div>
									</c:if>
								</li>
							</c:forEach>
							<!-- 댓글 들어가는곳 -->
						</ul>

						<div class="row">
						<form id="comment_form">
							<input name="voteNo" type="hidden" value="${vote.voteNo}">
							<div class="input-field col s8">
								<i class="material-icons prefix">account_circle</i>
								<input name="commentContent" id="comment" type="text" class="validate">
								<input type="text" style="display: none;" />
								<label for="comment">댓글</label>
							</div>
						</form>
							<div class="col s4">
								<div  id="add_comment_btn" class="btn btn-default">Add</div>
							</div>
						</div>

					</div>
				</div>
			</div>

		</div>
	</div>
	<script src='http://cdnjs.cloudflare.com/ajax/libs/d3/3.5.5/d3.min.js'></script>
	<script src='https://code.jquery.com/jquery-2.2.4.min.js'></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/js/materialize.min.js"></script>
	<script src="/result/resultMulti.js"></script>

	<script type="text/javascript">
	/* Enter 입력시 addComment Event 강제 발생 */
	$("#comment").keypress(function(e) { 
		if (e.keyCode == 13){
	        $('#add_comment_btn').trigger('click');
	     }    
	});
	
	
	$("#commentList").scrollTop($("#commentList")[0].scrollHeight);
	$("#add_comment_btn").on("click",function(){
		if($("#comment").val()==''){
			  Materialize.toast('댓글을 입력하세요. ', 3000,'pink accent-3')	
			  return;
		}
		
		
		
		var form = new FormData($("#comment_form")[0]);
		
		$.ajax({
			type : "post",
			url : "/comment/addComment",
			processData : false,/*data 파라미터로 전달된 데이터를 jQuery 내부적으로 query string 으로 만드는데, 파일 전송의 경우 이를 하지 않아야 하고 이를 설정하는 것이 processData: false 이다.*/
			contentType : false,/*contentType 은 default 값이 "application/x-www-form-urlencoded; charset=UTF-8" 인데, "multipart/form-data" 로 전송이 되게 false 로 넣어준다. */
			data : form,
			success : function(data){
				var parsedDate = new Date(parseInt(data.comment.regDate))
				var jsDate = new Date(parsedDate);
				var convertedDate= jsDate.getFullYear()+"-"+(jsDate.getMonth()+1)+"-"+jsDate.getDate()+" "+jsDate.getHours()+":"+jsDate.getMinutes()+":"+jsDate.getSeconds()+"."+jsDate.getMilliseconds();
				var row ="";
				row +=  "<li><div class='commenterImage'>";
				if(data.user.userPhoto.startsWith("fb_profile_image")){
					row +="<img src='"+data.user.userPhoto.replace("fb_profile_image","")+"'/></div>"
				}else{
					row +="<img src='/image/profile/thumbnail/"+data.user.userPhoto+"'/></div>"
				}
				row +="<div class='commentText'>";
				row +="<p class=''>"+data.comment.commentContent+"</p>";
				row +="<span class='date sub-text'>"+convertedDate+"</span></div>";
				row +="<div id='delete_comment_"+data.comment.commentNo+"' class='delete_btn_custom'><i class='material-icons'>remove_circle_outline</i></div></li>";
				$("#commentList").append(row);
				$("#comment").val("");

				$("#commentList").scrollTop($("#commentList")[0].scrollHeight);
			}
		});
	});



var genderData = []; 
var ageData = [];


	
	<c:forEach items="${mapList}" var="map">
		
			var gdata = new Object();
			gdata.State = ${map.get('choiceNo')};
			var gfreq = new Object()
			gfreq.male=${map.get('male')}
			gfreq.female=${map.get('female')}
			gdata.freq=gfreq;
			
			var adata = new Object();
			adata.State = ${map.get('choiceNo')};
			var afreq = new Object()
				afreq.s10=${map.get('s10')}
				afreq.s20=${map.get('s20')}
				afreq.s30=${map.get('s30')}
				afreq.s40=${map.get('s40')}
				afreq.s50=${map.get('s50')}
				afreq.s60=${map.get('s60')}
				adata.freq=afreq;

				genderData.push(gdata);
				ageData.push(adata);
	</c:forEach>

		dashboard('#dashboard', genderData, ageData);  



		$('#histogram .tick').each(function(){
					$(this).css("font-size","10px");
		});

 $('#histogram .tick>text').each(function(){
	  var choiceNo = $(this).text();
	  var content=''; 
	  var photo ='';
 	 $('input[id^=getText_]').each(function(){
 		var no = $(this).attr("id").replace("getText_",'');
 		if(choiceNo == no){
 			var id = '#getText_'+no;
 			content = $(id).val();
 		}
		
	 });
 	$('input[id^=getPhoto_]').each(function() {
		var no = $(this).attr("id").replace("getPhoto_", '');
		if (choiceNo == no) {
			var id = '#getPhoto_'+no;
			photo = $(id).val();
			
			
		}

	});
	$(this).addClass("tooltipped").attr("data-position",'bottom').
	attr("data-delay","50").attr('data-tooltip',"<img src='/image/vote/thumbnail/"+photo+"'>").
		attr('data-html','true');
 	$(this).text(content);
	

});    
 $('.tooltipped').tooltip({delay: 50});
	/*댓글 삭제*/
	
	$(document).on("click","div[id^='delete_comment_']",function(){
		var commentNo = $(this).attr("id").replace("delete_comment_","");
		$.ajax({
			type : "get",
			url : "/comment/deleteComment/"+commentNo
		});
		$(this).parent().remove();
	});

	</script>
</body>

</html>