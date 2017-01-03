//********************************************************
//**  Version  : 1.00                                   **
//**  detail   : 影像載入器			            		**
//**  Engineer : 曾勖華	                                **
//**  Release Date  : 2016/9/28                         **
//**  Generate Date : 2016/9/28                         **
//********************************************************

var player0 = document.getElementById('myVideo0');
var player1 = document.getElementById('myVideo1');
var stopFlag = 0;

function playvideo()
{
	playvideo0();
}
function playvideo1()
{　
 
	player1.play();

	setTimeout(preload0, 3000)
	setTimeout(playvideo0, 5000)
}	

function playvideo0()
{　
 
	player0.play();

	setTimeout(preload1, 3000)
	setTimeout(playvideo1, 5000)
}	
		
function preload1()
{　 if(stopFlag == 0)
	{
		
		$("#webmSource1").error(function() {
			/*
			var userName = getUserName();
			if(userName == "MP-4")
			{
				stopFlag=1;
				document.location.href="http://192.168.1.76:8787/Viewers";
			}
			
			else if(userName == "MP-5")	
			{
				stopFlag=1;
				document.location.href="http://192.168.1.68:8787/Viewers";
			}	
			
			console.log('load video error');
			
			var URL = "http://127.0.0.1:8787/error";

			$.ajax({
				  type : 'GET',
				  dataType : 'text',
				  url : URL,
				  success: function(msg){
								alert("出現錯誤轉址至 = " + msg);
							},
				  error:function(xhr, ajaxOptions, thrownError){ 
					 alert(xhr.status); 
					 alert(thrownError); 
				  }    
				  });
			*/  
		}).attr('src', "temp/video1.mp4");		  
	  
		player1.load();
		player1.pause();
	}
}   
		
function preload0()
{　 if(stopFlag == 0)
	{
		$("#webmSource0").error(function() {
			/*
			var userName = getUserName();
			if(userName == "MP-4")
			{
				stopFlag=1;
				document.location.href="https://192.168.1.76:8787/Viewers";
			}
			
			else if(userName == "MP-5")	
			{
				stopFlag=1;
				document.location.href="https://192.168.1.68:8787/Viewers";
			}	
			
			
			console.log('load video error');
			
			var URL = "http://127.0.0.1:8787/error";

			$.ajax({
				  type : 'GET',
				  dataType : 'text',
				  url : URL,
				  success: function(msg){
								alert("出現錯誤轉址至 = " + msg);
							},
				  error: function(xhr, ajaxOptions, thrownError){ 
					 alert(xhr.status); 
					 alert(thrownError); 
				  }    
				  });
			*/  
		}).attr('src', "temp/video0.mp4");		  
	  
		player0.load();
		player0.pause();
	}
}				