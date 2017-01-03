var express = require('express');
var app = express();
var ExpressPeerServer = require('peer').ExpressPeerServer;
var https = require('https');
var fs = require('fs');

var net = require('net');

var ssl ={                                         //公私鑰和憑證書 位置
	key:fs.readFileSync('ssl/mykey.pem'),
	cert:fs.readFileSync('ssl/my-cert.pem')
}

var server =  https.createServer(ssl, app);
var io = require('socket.io')(server);  

var options = {
	debug: true
}

app.use('/webrtcrecord', ExpressPeerServer(server, options));

server.listen(8787);

app.get(/(.*)\.(jpg|gif|png|ico|css|js|txt|webm|mp4)/i, function(req, res) {  
	if(req) return res.sendfile(__dirname + "/" + req.params[0] + "." + req.params[1], function(err) {
    });
});	

app.get('/LiveShow', function(req, res) {
	if(req) return res.sendfile(__dirname + '/html/LiveShow.html');   //影片直播錄製
});

app.get('/Viewers', function(req, res) {
	if(req) return res.sendfile(__dirname + '/html/Viewers.html');    //影片無縫串接 以撥放時間
});

//------------------------------------------------------------------------------------


io.on('connection', function(socket) {
	console.log('new connection');

    socket.on('new', function(data){ // 新的client登入
	   socket.name = data;       	// 設定新socket名稱為data
       });
	
	socket.on('blob', function(blob){ // 接收blob json物件{blob,tag}
	    console.log(blob);
		
		console.log(new Date());

		fs.writeFile("temp/video" + blob.tag + ".webm", blob.blob, function(err) {
			if(err) {
			  console.log("err", err);
			} else {
			  console.log("save");
			}
		  }); 			
		
       });

	socket.on('disconnect', function(){       //socket離線 

	});		  
});