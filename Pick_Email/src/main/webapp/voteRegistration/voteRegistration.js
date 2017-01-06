var n = "";
$(document).ready(function () {
	selectOptions();
    $('select').material_select();
    $('.datepicker').pickadate({
        selectMonths: true, // Creates a dropdown to control month
        selectYears: 15, // Creates a dropdown of 15 years to control year
        format: 'dd/mm/yyyy'
    });
});

// 이미지 업로드 미리보기
$(document).on('change', "input[id^=image]", function () {
    console.log($(this).val());
    var id = $(this).attr("id");
    var m = $(this).attr("id").replace("image", "");
    ext = $(this).val().split('.').pop().toLowerCase(); // 확장자
    // 배열에 추출한 확장자가 존재하는지 체크
   if($(this).val()==''){
	   $('#image_preview' + m).hide();
	   $(this).parent().children("label").text("ADD IMAGE");
    	return;
    }
    
    
    if ($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
        resetFormElement($(this)); // 폼 초기화
        window.alert('이미지 파일이 아닙니다! (gif, png, jpg, jpeg 만 업로드 가능)');
    }
    else {
        file = $(this).prop("files")[0];
        blobURL = window.URL.createObjectURL(file);
        $('#image_preview' + m + ' img').attr('src', blobURL);
        $('#image_preview' + m).slideDown(); // 업로드한 이미지 미리보기
        var img = $('#image_preview' + m).slideDown();
        $(this).slideUp(); // 파일 양식 감춤
        $(this).parent().prepend(img);
        $(this).parent().children("label").text("MODIFY");
    }
});


function resetFormElement(e) {
    e.wrap('<form>').closest('form').get(0).reset();
    // 리셋하려는 폼양식 요소를 폼(<form>) 으로 감싸고 (wrap()) ,
    // 요소를 감싸고 있는 가장 가까운 폼( closest('form')) 에서 Dom요소를 반환받고 ( get(0) ),
    // DOM에서 제공하는 초기화 메서드 reset()을 호출
    e.unwrap(); // 감싼 <form> 태그를 제거
}

$(document).on("click","#addExample",function(){
 
    var choiceCount=$("input[name^=content]").length;

    var row="<div class='row'>";
    row+="<div class='col s6'>";
    row+="<input type='text'  class='input_content' name='content" +(choiceCount+ 1) + "' placeholder='ENTER CONTENT'>";
    row+="</div>";
    row+="<div class='col s4' id='filebox'>";
    row+="<label for='image"+(choiceCount+ 1) +"' id='addaddadd'>ADD IMAGE</label>";
    row+="<input type='file' class='input_img'  accept='image/*' name='photo" + (choiceCount+ 1)  + "' id='image" + (choiceCount+ 1)  + "'style='display: none;' />";
    row+="<div id='image_preview" +(choiceCount+ 1)  + "' style='display:none'>"
    row+="<img src='#' class='circle' width='50px' height='50px'>";
    row+="</div>";
    row+="</div>";
    row+="<div class='col s2'><a id='remove_btn' class='btn-floating btn-large waves-effect waves-light red'><i id='removemeterial' class='material-icons'>remove</i></a></div>";
    row+="</div>";
    $("#multiChoice").append(row);
   
});

// 투표등록칸 삭제하기
$(document).on("click", "#remove_btn", function () {
    $(this).parent().parent().remove();
});

// 상세설정 보이기/숨기기
$("#showHide").click(function () {
    if ($("#optionalSelect").css("display") == "none") {
        jQuery('#optionalSelect').show();
    }
    else {
        jQuery('#optionalSelect').hide();
    }
});


function selectOptions() {
	$(".js-hide").find("input").attr("disabled","disabled");
	    
    $('.js-select-button').click(function () {

        $('.js-select-button').toggleClass('btn--active');  
        $('.js-select').hide();
        $('.js-' + $(this).val()).show();
        $('.js-select').find("input").attr("disabled","disabled"); 
																	
        $('.js-' + $(this).val()).find("input").removeAttr("disabled"); 
																	
        $("#voteType-select").val($(this).val());
        console.log($(this).val());
             

    });
   
}



