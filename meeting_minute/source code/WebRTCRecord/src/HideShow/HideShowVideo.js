//********************************************************
//**  Version  : 1.01                                   **
//**  detail   : vidoe元件控制器			            **
//**  Engineer : 曾勖華	                                **
//**  Release Date  : 2016/9/28                         **
//**  Generate Date : 2016/9/28                         **
//********************************************************

function myHandler1(e)
{
	if(!e) 
	{
		e = window.event; 
	}       
	$("#myVideo0").hide();
	$("#myVideo1").show();
 
}


function myHandler0(e)
{

	if(!e){
		e = window.event; 
	}
	
	$("#myVideo1").hide();
	$("#myVideo0").show();
           
}