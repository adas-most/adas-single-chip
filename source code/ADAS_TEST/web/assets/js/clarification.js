"use strict";

/*
    intitial
*/
$(function()
{
    /*** 取得 OBD2 感測資料 ***/
   $.post(
		   "/OBD2_getSensorData",
	        "{}"
    )
    .done(
        function(sensordata)
        {
            console.log(sensordata);
            console.log(SensorDataText);

            $("#OBD2SensorTable tbody").empty();

            $.each(sensordata, function(key, data)
            {
                var tb = "<tr>"
                    + "<th scope='row'>" + SensorDataText[key] + data.Unit + "</th>"
                    + "<td nowrap>" + data.Value + "</td>"
                    + "</tr>";

                $("#OBD2SensorTable tbody").append(tb);
            });
        }
    )
    .fail( function(data) { console.log(data); } )

   /*** 取得影像處理模組名稱 ***/
   $.post(
		   "/IPM_getModuleParam",
	        "{}"
    )
    .done(
        function(moduleparam)
        {
            console.log(moduleparam);

            $("#ipmTable tbody").empty();

            $.each(moduleparam, function(key, module)
            {
                var tb = "<tr>"
                    + "<td nowrap>" + ImageProcessingModuleText[key] + "</td>"
                    + "<td nowrap>"
                    + "<div class='checkbox checkbox-success'><input class='styled' type='checkbox'  id='" + key + "'><label></label></td>"
                    + "</div>"
                    + "</td>"
                    + "</tr>";

                $("#ipmTable tbody").append(tb);
            });
        }
    )
    .fail( function(data) { console.log(data); } )

    /*** 取得事件類型 ***/
   $.post(
        "eventtype_getoption",
        "{}"
    )
    .done(
        function(eventtype)
        {
            console.log(eventtype);

            $("#eventType option").remove();

            $("#eventType").append("<option value='' selected disabled>請選擇</option>");

            $.each(eventtype, function(index, type)
            {
                $("#eventType").append($("<option></option>").val(type).text(type));
            });
        }
    )
    .fail( function(data) { console.log(data); } )
});



/*
    影像處理模組表格(id='ipmTable')
*/
var confirmUseModules = new Object();

$(function()
{
    /*** 確認按鈕 click ***/
    $("#btn_ipmTable_confirm").click(function()
    {
        var checkFlag = false;

        // 1. 是否有選擇事件類型
        if($("#eventType").val() == null)
        {
            alert("請選擇事件類型!");
            $("#eventType").focus();

            return false;
        }
        else
        {
            confirmUseModules.EventType = $("#eventType").val();
        }

        console.log(confirmUseModules);

        // 2. 遍歷 checkbox 寫入 json
        $("#ipmTable input:checkbox").each(function()
        {
            if($(this).prop("checked"))
            {
                checkFlag = true;
                confirmUseModules[$(this).attr("id")] = true;
            }
            else
            {
                confirmUseModules[$(this).attr("id")] = false;
            }
        });

        // 3. 是否有選擇至少一項影像處理模組
        if(checkFlag)
        {
            // 提示訊息給使用者
            Alert_Info("alert alert-info", "釐清中...");
            $("#Alert_state i").show();
            
           $.post(
                "/IPM_confirmUseModules", 
                JSON.stringify(confirmUseModules) + '$'
            )
            .done( 
                    function(data)
                    { 
                        Alert_Info("alert alert-success", "釐清完成！");
                        $("#Alert_state i").hide();
                        setTimeout('Redirect()', 1500);
                    }
            )
            .fail( 
                    function(data)
                    {
                        Alert_Info("alert alert-danger", "伺服器錯誤！");
                        $("#Alert_state i").hide(); 
                        console.log(data); 
                    }
            )
        }
        else
        {
            alert("請選擇至少一項影像處理模組!");

            return false;
        }
    });

    /*** 取消按鈕 click ***/
    $("#btn_ipmTable_cancel").click(function()
    {
        // 2. 遍歷 checkbox 取消勾選
        $("#ipmTable input:checkbox").each(function()
        {
            $(this).prop("checked", false);
        });
    }); 
});