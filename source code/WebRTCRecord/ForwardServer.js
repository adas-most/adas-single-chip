var express = require('express');
var app = express();
var ExpressPeerServer = require('peer').ExpressPeerServer;
var https = require('https');
var fs = require('fs');

var ssl ={                                         //公私鑰和憑證書 位置
	key:fs.readFileSync('ssl/mykey.pem'),
	cert:fs.readFileSync('ssl/my-cert.pem')
}

var server =  https.createServer(ssl, app);
var io = require('socket.io')(server);  

var options = {
	debug: true
}

var now = new Date();

app.use('/webrtcrecord', ExpressPeerServer(server, options));

server.listen(8787);

app.get(/(.*)\.(jpg|gif|png|ico|css|js|txt|webm)/i, function(req, res) {  
	res.sendfile(__dirname + "/" + req.params[0] + "." + req.params[1], function(err) {
    });
});	

app.get('/Viewers', function(req, res) {
	res.sendfile(__dirname + '/html/Viewers.html');    //影片無縫串接 以撥放時間
});