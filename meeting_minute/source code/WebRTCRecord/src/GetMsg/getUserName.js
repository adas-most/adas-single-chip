//********************************************************
//**  Version  : 1.00	                                **
//**  detail   : 取得使用者名稱			            	**
//**  Engineer : 曾勖華	                                **
//**  Release Date  : 2016/10/24                        **
//**  Generate Date : 2016/10/24                        **
//********************************************************
var UserName;
function getUserName()
{

var URL = "http://127.0.0.1:8787/UserName";

$.ajax({
      type : 'GET',
      dataType : 'text',
      url : URL,
	  success: function(msg){
                    UserName = msg;
					alert("UserName = " + UserName);
					return UserName;
                },
      error:function(xhr, ajaxOptions, thrownError){ 
         alert(xhr.status); 
         alert(thrownError); 
      }    
	  });
}
