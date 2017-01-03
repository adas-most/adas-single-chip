//********************************************************
//**  Version  : 1.01                                   **
//**  detail   : WebRTC串流播放模組			            **
//**  Engineer : 曾勖華	                                **
//**  Release Date  : 2016/9/23                         **
//**  Generate Date : 2016/9/26                         **
//********************************************************

// 建立 WebRTC 連線
/*
@param mediaConstraints		訪問的媒體類型
@param successCallback		送入的 video 串流與相關設定
@param errorCallback		error訊息
*/

var tag=1; // 檔名的counter

// 進行錄影
function startRecord(stream){　 
	// 檔名的counter++
	tag++; 
	
	// 設定串資料來源（影像）
	window.audioVideoRecorder = window.RecordRTC(stream, {
                  type: 'video'
              });
		
	// 開始取得串流資料（影像）
	window.audioVideoRecorder.startRecording();

	// 每 5 秒執行一次 停止錄影 function
	setTimeout(stopRecord, 5000)
	
	// 每 5.01 秒執行一次 進行錄影 function
	setTimeout(function(){startRecord(stream);}, 5010)
}		

// 停止錄影
function stopRecord( ){　 
	// 停止取得串流資料
	window.audioVideoRecorder.stopRecording(function(url) {
	
		// 傳送串流資料至 server 
		socket.emit('blob',{blob:audioVideoRecorder.getBlob(),tag:tag%2}); //send blob to server {blob,tag%2(0or1)}
		//socket.emit('ok',"ok");

		});	
	 		　 
}

function captureUserMedia(mediaConstraints, successCallback, errorCallback) {
    navigator.mediaDevices.getUserMedia(mediaConstraints).then(successCallback).catch(errorCallback);
}