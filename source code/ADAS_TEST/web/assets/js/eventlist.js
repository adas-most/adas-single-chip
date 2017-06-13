// 事件處理狀態之按鈕設定
var btnText  = ["等待釐清中", "等待處理中"];
var btnClass = ["btn btn-md btn-warning", "btn btn-md btn-success"];
var btnHerf  = ["./Clarification.html", "./Processing.html"];

$(function()
{
    "use strict";//使用strict mode(嚴格模式)
    
   $.post(
        "/eventlist_getform",
        "{}"
    )
    .done(
        function(eventlist)
        {
            // console.log(data);

            $("#eventListTable tbody").empty();

            var index = 0;
            $.each(eventlist, function(key, event)
            {
                index++;

                var tb = "<tr>"
                    + "<th scope='row'>" + index + "</th>"
                    + "<td nowrap>" + event.time + "</td>"
                    + "<td nowrap>" + event.position + "</td>"
                    + "<td nowrap><a type='button' target='_self' href='" + btnHerf[event.status] + "?" + event.eventid + "' class='" + btnClass[event.status] + "'>" + btnText[event.status] + "</td>"
                    + "</tr>";

                $("#eventListTable tbody").append(tb);
            });
        }
    )
    .fail( function(data) { console.log(data); } )

});
