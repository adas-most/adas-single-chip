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
	
    /*** 取得影像處理模組參數 ***/
   $.post(
        "/IPM_getModuleParam",
        "{}"
    )
    .done(
        function(moduleparam)
        {
            console.log(moduleparam);

            $("#ipmTable tbody").empty();

            $.each(moduleparam, function(modkey, module)
            {
                var firstParam = true;
                $.each(module.param, function(paramkey, param)
                {
                    var tb = "<tr>";

                    if(firstParam)
                    {
                        tb += "<th scope='row' rowspan='" + Object.keys(module.param).length + "'>" + ImageProcessingModuleText[modkey] + "</th>";
                        firstParam = false;
                    }

                    tb += "<td nowrap class='param-font-size' name='" + modkey + "'>" + ParameterText[paramkey] + "</td>";
                    tb += "<td nowrap name='param_value'>" + param.value + "</td>";
                    tb += "<td nowrap><input type='text' name='" + paramkey + "' class='form-control input-modify'></td>";
                    tb += "</tr>";

                    $("#ipmTable tbody").append(tb);
                });
            });
        }
    )
    .fail( function(data) { console.log(data); } )

    /*** 取得 處理方法陳述 資料 ***/
    $.post(
        "/treatmentstatement_getform",
        "{}"
    )
    .done(
        function(statementlist)
        {
            console.log(statementlist);
			
            $("#tsTable tbody").empty();
			
            $.each(statementlist, function(index, statement)
            {
                var tb = "<tr>";
                tb += "<td nowrap>" + statement + "</td>";
                tb += "<td nowrap><div class='checkbox checkbox-danger'><input class='styled' type='checkbox'><label></label></div></td>";
                tb +=  "</tr>";

                $("#tsTable tbody").append(tb);
            });
        }
    )
    .fail( function(data) { console.log(data); } )
	
	
    /*** 取得 相同事件類型 資料 ***/
    $.post(
        "/relate",
        "{}"
    )
    .done(
        function(sometypelist)
        {
            console.log(sometypelist);

            $("#someTypeTable tbody").empty();
            $("#lb_SomeTypeList").empty();
			
			$("#lb_SomeTypeList").append("事件類型 : " + sometypelist.eventtype);
            delete sometypelist["eventtype"];
			
			$.each(sometypelist, function(index,event)
            {
                var tb = "<tr>"
                    + "<th scope='row'>" + event.eventid + "</th>"
                    + "<td nowrap>" + event.time + "</td>"
                    + "<td nowrap>" + event.position + "</td>"
                    + "<td nowrap><a href='CompletedEvent.html?" + event.eventid + "' target='_blank'>Go</a></td>"
                    + "</tr>";

                $("#someTypeTable tbody").append(tb);
            });
        }
    )
    .fail( function(data) { console.log(data); } )
});



/*
    處理方法陳述表格(id='tsTable')
*/
$(function()
{
    /*** 刪除按鈕 click ***/
    $("#btn_tsTable_del_method").click(function()
    {
        $("#tsTable input:checkbox").each(function(index)
        {
            if($(this).prop("checked"))
            {
                $(this).closest('tr').remove();
            }
        });
    });

    /*** 新增按鈕 click ***/
    $("#btn_tsTable_new_method").click(function(event)
    {
        event.preventDefault();
        $('#modal_AddNewMethod').modal("show");
    }); 
});
    
    

/*
    新增處理方法視窗(id='modal_AddNewMethod') function
*/
$(function()
{
    /*** '新增處理方法'視窗 顯示前 call ***/
    // $("#modal_AddNewMethod").on('show.bs.modal', function (){});

    /*** '新增處理方法'視窗 隱藏前 call ***/
    // $("#modal_AddNewMethod").on('hide.bs.modal', function (){});

    /*** '查詢關鍵字'輸入框 enter ***/
    $("#form_AddNewMethod").submit(function(event)
    {
        event.preventDefault();
        console.log("keyWord : " + $("#newMethodKeyWord").val());
    });

    /*** 加入按鈕 click ***/
    $("#btn_add_method").click(function()
    {
        // console.log("'新增處理方法' 加入按鈕 click.");

        $("#form_AddNewMethod input:checkbox").each(function(index)
        {
            if($(this).prop("checked"))
            {
                var tb = "<tr>";
                tb += "<th scope='row'>" + $(this).closest('td').prev().html() + "</th>";
                tb += "<td nowrap><div class='checkbox checkbox-danger'><input class='styled' type='checkbox' id='" + $(this).attr("id") + "''><label></label></div></td>";
                tb +=  "</tr>";

                $("#tsTable").append(tb);
            }
        });
    });

    /*** 建立新處理方法按鈕 click ***/
    $("#btn_create_method").click(function()
    {
        // console.log("'新增處理方法' 建立新處理方法按鈕 click.");

        $('#div_new_method_content').show();
    });

    /*** 確認按鈕 click ***/
    $("#btn_new_method_confirm").click(function()
    {
        var newMethod = $("#text_new_method_content").val().trim();
        console.log("newMethod : " + newMethod);
        

        // 1. 新增至'建立新處理方法'表格中
        var tb = "<tr>";
        tb += "<td nowrap>" + newMethod + "</td>";
        tb += "<td nowrap><div class='checkbox checkbox-success'><input class='styled' type='checkbox' checked><label></label></div></td>";
        tb +=  "</tr>";

        $("#newtsTable").append(tb);
    });
});



/*
    影像處理模組表格(id='ipmTable')
*/
$(function()
{
    /*** 確認按鈕 click ***/
    $("#btn_ipmTable_confirm").click(function()
    {
        var modifyModulesParameter = new Object();
        var cnt = 0;

        // 1. 寫入修正值
        $("#ipmTable input:text").each(function()
        {
            var ModuleName = $(this).closest('tr').children("td").attr("name");
            if(modifyModulesParameter[ModuleName] == null)
            {
                modifyModulesParameter[ModuleName] = new Object();
                modifyModulesParameter[ModuleName].param = new Object();
            }

            modifyModulesParameter[ModuleName].param[$(this).attr("name")] = new Object();

            if($(this).val().trim() == "")
            {
                modifyModulesParameter[ModuleName].param[$(this).attr("name")].value = $(this).closest('td').prev().html();
            }
            else
            {
                modifyModulesParameter[ModuleName].param[$(this).attr("name")].value = $(this).val();
            }
        });
        // 

        // 2. 寫入處理方法陳述
        $("#btn_tsTable_del_method").click();
        modifyModulesParameter.statement = new Array();
        $("#tsTable input:checkbox").each(function(index)
        {
            if(!$(this).prop("checked"))
            {
                modifyModulesParameter.statement[index] = $(this).closest('td').prev().html();
            }
        });

        console.log(JSON.stringify(modifyModulesParameter));


        // 提示訊息給使用者
        Alert_Info("alert alert-info", "處理中...");
        $("#Alert_state i").show();

        // 3. 回傳修正值
       $.post(
            "/IPM_modifyModulesParameter", 
            JSON.stringify(modifyModulesParameter) + '$'
        )
        .done(
            function(data)
            { 
                Alert_Info("alert alert-success", "處理完成！");
                $("#Alert_state i").hide();
                setTimeout('Redirect(1)', 1500);
            } 
        )
        .fail(
            function(data)
            { 
                Alert_Info("alert alert-danger", "伺服器錯誤！");
                $("#Alert_state i").hide(); 
            }
        )
    });

    /*** 取消按鈕 click ***/
    $("#btn_ipmTable_cancel").click(function()
    {
        $("#ipmTable input:text").each(function()
        {
            $(this).val("");
        });
    }); 
});



/*
    尋找相同事件類型(id='someTypeTable')
*/
$(function()
{
    /*** 尋找相同事件類型 按鈕 click ***/
    $("#btn_SomeTypeList").click(function(event)
    {
        event.preventDefault();
		
        $('#modal_SomeTypeList').modal("show");
    }); 
});