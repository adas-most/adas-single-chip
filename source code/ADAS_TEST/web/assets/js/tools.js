//title的提示訊息
$(function () {
  //tooltip要初始化
  $('[data-toggle="tooltip"]').tooltip()
})

//提示訊息
function Alert_Info(class_str, label_str)
{
    $("#Alert_state").attr("class",class_str);
    $("#Alert_label").html(label_str);
    $("#Alert_state").show("2000");    
}

//自動導回首頁
function Redirect()
{
    window.location = '/eventlist.html';
}
