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
                tb +=  "</tr>";

                $("#tsTable tbody").append(tb);
            });
        }
    )
    .fail( function(data) { console.log(data); } )
});