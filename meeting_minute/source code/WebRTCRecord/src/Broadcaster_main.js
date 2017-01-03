//********************************************************
//**  Version  : 1.01                                   **
//**  detail   : 直播實況系統-實況主			        **
//**  Engineer : 曾勖華	                                **
//**  Release Date  : 2016/9/23                         **
//**  Generate Date : 2016/9/26                         **
//********************************************************

$(function()
{
	var videoElement = document.getElementById('video');
	
	//預設 - 啟用 開始錄影 按鈕
	$("#start-recording").attr('disabled', false);
	
	// 按下開始錄影後的事件
    $("#start-recording").click(function(){

        $("#start-recording").attr('disabled', true);	// 關閉 開始錄影 按鈕
	
	    // 呼叫 function captureUserMedia 建立 WebRTC 連線
        captureUserMedia({ 	// 訪問的媒體類型
		
			audio: true,	// 音訊			
            video: true 	// 影像
		
        }, function(stream) {	// 送入的 video 串流與相關設定
		
		$("#video").attr("src", URL.createObjectURL(stream));	// 開啟影像並設定網址		
		$("#video").attr("muted", true);						// 初始設定為靜音
		$("#video").attr("controls", false);						// 初始設定為可控制
		$("#video").trigger('play');							// 開始播放

		startRecord(stream);
		
        }, function() {	// 錯誤訊息
	
		$("#start-recording").attr('disabled', false);	// 啟用 開始錄影 按鈕
        alert("實況出現錯誤"); // 跳出彈窗顯示錯誤訊息
		
        });
			
    });

});