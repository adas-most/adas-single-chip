//********************************************************
//**  Version  : 1.00                                   **
//**  detail   : ������p�t��-�[��			            **
//**  Engineer : ���m��	                                **
//**  Release Date  : 2016/9/28                         **
//**  Generate Date : 2016/9/28                         **
//********************************************************
var UserName;

function getTextBoxUserName()
{
	UserName = document.getElementById('TextBox').value;
	
	alert("Registered user : " + UserName);

	$("#myVideo0").show();
	$("#myVideo1").hide();
	
	start();
		
}

$(function start()
{
	playvideo();
   
	player0.addEventListener('ended',myHandler1,false);
   
	player1.addEventListener('ended',myHandler0,false);


}
)

function getUserName()
{
	return UserName;
}	