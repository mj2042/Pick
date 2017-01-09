var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var httpClient = require('httpclient');
var userKey;
var jsonObj;
var strObj;
var obj;
var name;
var photoPath;
var savename;
var closename;
var userlist = [];
var voteTitle = '';
var filtermsg = '';
//톰캣에서 redirect 될때 채팅페이지로 이동 및 applicationScope에 담긴 유저정보의 키값받기
app.get('/chatServer/:key', function(req, res, next) {
	res.sendFile(__dirname + '/chat.html');
	userKey = req.params.key;
	console.log("userKey 는 ? :", userKey);

	//httpClient 모듈사용하여 톰캣에 요청하기 (Json Data 받기 ==> httpclient 외부모듈사용)
	var options = {
		hostname : '52.78.201.215',
		path : '/',
		port : 8080,
		secure : false,
		method : 'GET',
		Type : 'application/json',
		headers : {
			'x-powered-by' : 'HTTPClient.js'
		},
	}
	console.log('options : ', options)
	var chatObj = new httpClient(options)
	chatObj.request('/chat/userJsonObject/' + userKey, function(err, resp2,
			body) {
		jsonObj = JSON.parse(body.toString())
		name = jsonObj.userName //유저이름
		photoPath = jsonObj.userPhoto //프로필사진경로
	});
});
//채팅을 위한 소켓통신 ==> emit , broadcast 
io.on('connection', function(socket) {
	if (jsonObj == null || jsonObj == undefined || jsonObj == '') {
		console.log("잘못된 접근입니다.");
		return;
	}
	console.log("JSONobj 확인", jsonObj);
	socket.on('chat message', function(msg, name) {
		console.log("[" + name + "] : ", msg);
		if (msg.match('#투표')) {
			filtermsg = msg.substring(4,9)
			console.log("[투표참여하기] :", filtermsg + "번 투표에 참가합니다.");
			//httpClient 모듈사용하여 톰캣에 요청하기 (Json Data 받기 ==> httpclient 외부모듈사용)
			var options = {
				hostname : '52.78.201.215',
				path : '/',
				port : 8080,
				secure : false,
				method : 'GET',
				Type : 'application/json',
				headers : {
					'x-powered-by' : 'HTTPClient.js'
				},
			}
			console.log('options : ', options);
			var chatObj = new httpClient(options);
			chatObj.request('/chat/getVotebyChatServer/' + filtermsg, function(err,
					resp2, body) {
				var jsonVoteObj = JSON.parse(body.toString())
				voteTitle = jsonVoteObj.voteTitle //투표제목
				console.log("받아온 JSON의 voteTitle : ", voteTitle);
				socket.emit("voteTitle", voteTitle , filtermsg);
				socket.broadcast.emit("tagbroadcast", voteTitle, filtermsg);
			});
			return;
		}
		if(msg.match("#내정보")){
			socket.emit("myInfo" , userKey);
		}
		socket.broadcast.emit('talker name', name);
		socket.broadcast.emit("broadcast", msg, name);
	});
	socket.emit('enter', name, photoPath, userKey);
	io.emit('enterAlarm', name, userKey);

	var CheckArr = userlist.filter(function(item) {
		return item.userKey === userKey;
	});
	console.log("checkArr 는 :: ", CheckArr);
	console.log("checkArr 의 length : ", CheckArr.length);
	if (CheckArr.length != 1) {
		console.log("userlist 중복체크결과 ::  중복이 되지 않으므로 userlist에 추가합니다.");
		userlist.push({
			"name" : name,
			"userKey" : userKey,
			"photoPath" : photoPath
		});
	} else if (CheckArr.length >= 1) {
		console.log("userlist 중복체크결과 :: 이미 list에 추가된 유저이므로 list에 추가하지 않습니다.");
	}
	console.log("userlist : ", userlist);
	io.emit("userlist", userlist);
	socket.on("leave" , function(userKey, name){
		 console.log("여기는 exit " , userKey, name);
	    	console.log(":::::: ["+userKey +"] 의 정보를 userlist에서 삭제합니다. ::::::");
	    	var filterArr = userlist.filter(function(item){
	    		return item.userKey === userKey;
	    	});
	    	console.log("filterArray : " , filterArr);
	    	var removeIndex = userlist.indexOf(filterArr[0]);
	    	console.log("removeIndex : " , removeIndex);
	    	if(removeIndex != -1){
	    	userlist.splice(removeIndex, 1);
	    	console.log("방 나간 유저 삭제 후 userlist : " , userlist);
	    	io.emit("userlist" , userlist);
	    	io.emit("exitAlarm" , name, userKey);
	    	}else{return;}
	});
});


http.listen(3000, function() {
	console.log('3000포트 연결됨.');
});
