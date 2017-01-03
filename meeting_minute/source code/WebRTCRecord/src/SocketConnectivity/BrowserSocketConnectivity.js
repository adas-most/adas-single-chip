//********************************************************
//**  Version  : 1.01                                   **
//**  detail   : Browser Socket聯接器					**
//**  Engineer : 曾勖華	                                **
//**  Release Date  : 2016/9/23                         **
//**  Generate Date : 2016/9/26                         **
//********************************************************
var obj = new Object; // 建立直播準備完成訊息

var socket = io.connect();	// 建立 socket 物件並連接

$(function() {

	obj.name = "0w0"; // 寫入訊息內容

	socket.on('connect', function() {                			  		
			socket.emit('new',obj.name); // 直播準備完成訊息
	}); 

});