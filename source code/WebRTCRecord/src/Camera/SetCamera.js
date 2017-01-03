//********************************************************
//**  Version  : 1.01                                   **
//**  detail   : 攝影機確認器			            	**
//**  Engineer : 曾勖華	                                **
//**  Release Date  : 2016/9/23                         **
//**  Generate Date : 2016/9/26                         **
//********************************************************

// 判斷攝影機是否存在
$(function()
{ 
	if (hasGetUserMedia()) {
		alert('攝影機載入成功');
	} else {
		alert('未發現攝影機');
	}
});// END ALL	

// 回傳攝影機狀況
function hasGetUserMedia() {
  // Note: Opera builds are unprefixed.
  return !!(navigator.getUserMedia || navigator.webkitGetUserMedia ||
			navigator.mozGetUserMedia || navigator.msGetUserMedia);
}