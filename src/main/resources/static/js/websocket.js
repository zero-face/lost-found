var stompClient = null;
var wsCreateHandler = null;
var userId = 1;
var ipAddr = null;
function connect() {
	var host = window.location.host; // 带有端口号
	userId =  GetQueryString("userId");
	console.log(ipAddr);
	var socket = new SockJS("http://" + ipAddr + "/websocket");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function (frame) {
		console.log("连接成功");
		writeToScreen("connected: " + frame);
		// stompClient.subscribe('/topic', function (response) {
		// 	writeToScreen(response.body);
		// });
		//
		// stompClient.subscribe("/queue/" + userId + "/topic", function (response) {
		// 	writeToScreen(response.body);
		// });
		//
		// stompClient.subscribe('/sendToAll', function (response) {
		// 	writeToScreen("sendToAll:" + response.body);
		// });
		console.log("end");
		stompClient.subscribe("/exchange/sendToUser/" + 1, function (response) {
			writeToScreen(response.body);
		});
		}, function (error) {
			wsCreateHandler && clearTimeout(wsCreateHandler);
			wsCreateHandler = setTimeout(function () {
				console.log("重连...");
				connect();
				console.log("重连完成");
			}, 1000);
		}
	)
}

function disconnect() {
	if (stompClient != null) {
		stompClient.disconnect();
	}
	writeToScreen("disconnected");
}

function writeToScreen(message) {
	if(DEBUG_FLAG)
	{
		$("#debuggerInfo").val($("#debuggerInfo").val() + "\n" + message);
	}
}

function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg); //获取url中"?"符后的字符串并正则匹配
	var context = "";
	if (r != null)
		context = r[2];
	reg = null;
	r = null;
	return context == null || context == "" || context == "undefined" ? "" : context;
}
