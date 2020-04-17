/*
 * Create By:    ywj
 * Create Date:  2018/10/25 20:43
 * Desc:         打印网页
 *
 * */

/**
 * Autor:ywj
 * Date:2018/10/25
 * Time:20:45
 * 打印判断
 * @param printData
 * @constructor
 */
function NameItemPrint(printData) {

    // 先进行判断该条码是否已经打印过，如果打印过则直接打印 如果没有打印则获取服务器照片存储路径
    var fileName = printData.text + "-sq.png";
    var isPrint = postAjax("/is/printDis", {fileName: fileName, printName: printData.printName});
    if (isPrint.success) {
        return;
    } else {
        if (isPrint.message != null && isPrint.message != "" && isPrint.message != undefined) {
            alert(isPrint.message);
            return;
        }
    }
    var nameplate;
    var date = new Date().Format("yyyy/MM/dd hh:mm");
    if (printData.docType == 'PREPARATION_DOC') {
        var headData = getHeadData("/hwm/item/distribute/line/queryItemDistribute", printData.text);
        var lineData = getLineData("/hwm/item/distribute/line/queryForPrint", printData.text);
        var codeImgId = "asnBarCode" + printData.index;
        var codeTd = "barCode" + printData.index;
        var lineInf = '';
        var atrribute = headData.attribute1;
        atrribute = atrribute.substring(0,2);
        var projectItemDesc = headData.projectItemDesc?headData.projectItemDesc.substring(0,10):"";
        nameplate =  '<br>'+
            '<br>'+
            '<div id="nameplate" style="width: 120%;height: 200%; background-color: #FFF;font-size: 25px;">' +
            '<table class="titleDiv" style="width: 100%;height: 5%;color: black;">' +
            '<tr style="width: 100%;height: 5%;">' +
            '</tr>' +
            '<tr style="width: 100%;height: 5%;">' +
            '<td style="width: 100%;text-align: center">' +
            '<span style="font-size: 50px;text-align: center">备料单</span>' +
            '</td>' +
            '</tr>' +
            '<tr>' +
            '</tr>' +
            '</table>' +
            ' <table class="mainDiv" style="width: 100%;height: 10%;color: black;">' +
            '<tr  style="width: 100%;font-weight:bold">' +
            '<td style="width: 40%;text-align: center" rowspan="3"  id=' + codeTd + '>' +
            '<div id='+ codeImgId + '>'+
            '</td>' +
            '<td style="width: 30% ">' +
            '<span style="white-space: nowrap">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>备料单号：' +headData.distruibuteNumber + '</b></span>'+
            '</td>' +
            '<td style="width: 30% ">' +
            '<span style="white-space: nowrap"><b>项目物料：'  + projectItemDesc + '</b></span>' +
            '</td>'+
            '</tr>' +
            '<tr style="width: 100%;font-weight:bold">' +
            '<td style="width: 33%;">' +
            '<span></span>' +
            '<span></span>' +
            '</td>' +
            '<td style="width: 33%;">' +
            '<span></span>' +
            '<span></span>' +
            '</td>' +
            '</tr>' +
            '<tr style="width: 100%;font-weight:bold">' +
            '<td style="width: 33%">' +
            '<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>仓库：</b></span>' +
            '<span style="font-size: 25px;">&nbsp;&nbsp;&nbsp;' +atrribute +"仓库"+ '</span>' +
            '</td>' +
            '</tr>' +
            '<tr style="width: 100%;font-weight:bold">' +
            '<td style="width: 33%;">' +
            '<span>&nbsp;&nbsp;&nbsp;</span>' +
            '<span>&nbsp;&nbsp;&nbsp;</span>' +
            '</td>' +
            '</tr>' +
            '</table>' +
            ' <table class="mainDiv" style="width: 100%;height: 10%;color: black;">' +
            '<tr style="width: 100%;font-weight:bold">' +
            '<td style="width: 33%;">' +
            '<span><b>备料打印时间：</b></span>' +
            '<span style="font-size: 21px">&nbsp;' +date + '</span>' +
            '</td>'+
            '<td style="width: 33%">' +
            '<span>&nbsp;<b>最迟发车时间：</b></span>' +
            '<span style="font-size: 21px">&nbsp;' +headData.latestTime + '</span>' +
            '</td>' +
            '<td style="width: 33%;padding-left: -10%; white-space: nowrap">' +
            '<span ><b>预计到达时间：</b></span>' +
            '<span style="font-size: 21px">&nbsp;' +headData.arrivedTime + '</span>' +
            '</td>' +
            '</tr>' +
            '</table>' +
            '<br>' +
            '<table class="myTable"  style=" border: 3px solid black;  color: black;width: 100%;table-layout: fixed;font-size: 20px;text-align: center;" border="2px">'+
            '<tr style="white-space: nowrap">'+
            '<th style="text-align: center;width: 5%;">序号</th>'+
            '<th style="text-align: center;width: 24%;">零件编码</th>'+
            '<th style="text-align: center;width: 20%;">零件名称</th>'+
            '<th style="text-align: center;width: 13%;">仓库货位</th>'+
            '<th style="text-align: center;width: 12%;">计划备料<br>数量</th>'+
            '<th style="text-align: center;width: 12%;">计划备货<br>箱数</th>'+
            '<th style="text-align: center;width: 12%;">实际备料<br>数量</th>'+
            '<th style="text-align: center;width: 7%;">备料人</th>'+
            '<th style="text-align: center;width: 10%;">备注</th>'+
            '</tr>';
        var asd=1;
        var b = 1;
        b = bye(lineData.length);
        for(var i = 0 ; i<lineData.length;i++)
        {
            var attract = (lineData[i].attribute1).split('#');
            lineData[i].attribute1 = attract[0];
            if(i%30==0&&i!=0){
                nameplate =  nameplate+ '</table>'+
                    '<table class="titleDiv" style="width: 100%;height: 5%;color: black;">' +
                    '<tr style="width: 100%;height: 5%;">' +
                    '</tr>' +
                    '<tr style="width: 100%;height: 5%;">' +
                    '<td style="width: 100%;text-align: center">' +
                    '<span style="font-size: 20px;padding-left:200px;text-align: center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>'+'             '+asd+'/'+b+'</b></span>' +
                    '</td>' +
                    '</tr>' +
                    '<tr>' +
                    '</tr>' +
                    '</table>' +
                    '</div>';
                asd++;
                $("#nameplateCode").html(nameplate); // 设置条码内容
                $('#' + codeImgId).barcode(
                    headData.distruibuteNumber ,
                    "code128",
                    {
                        barWidth: 3,
                        barHeight: 120,
                        showHRI: false,
                        fontSize: 10
                    }
                );
                // 设置名称
                $("#nameplate").show();// 显示
                getImgStr("nameplateCode", i+'-'+printData.text + "-sq.png", isPrint.code, printData.printName);

                nameplate = '<br>'+
                    '<br>'+
                    '<div id="nameplate" style="width: 120%;height: 200%; background-color: #FFF;font-size: 25px;">' +
                    '<table class="titleDiv" style="width: 100%;height: 5%;color: black;">' +
                    '<tr style="width: 100%;height: 5%;">' +
                    '</tr>' +
                    '<tr style="width: 100%;height: 5%;">' +
                    '<td style="width: 100%;text-align: center">' +
                    '<span style="font-size: 50px;text-align: center">备料单</span>' +
                    '</td>' +
                    '</tr>' +
                    '<tr>' +
                    '</tr>' +
                    '</table>' +
                    ' <table class="mainDiv" style="width: 100%;height: 10%;color: black;">' +
                    '<tr  style="width: 100%;font-weight:bold">' +
                    '<td style="width: 40%;text-align: center" rowspan="3"  id=' + codeTd + '>' +
                    '<div id='+ codeImgId + '>'+
                    '</td>' +
                    '<td style="width: 30% ">' +
                    '<span style="font-size: 25px; white-space: nowrap">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>备料单号：' +headData.distruibuteNumber + '</b></span>' +
                    '</td>' +
                    '</tr>' +
                    '<tr style="width: 100%;font-weight:bold">' +
                    '<td style="width: 33%;">' +
                    '<span></span>' +
                    '<span></span>' +
                    '</td>' +
                    '<td style="width: 33%;">' +
                    '<span></span>' +
                    '<span></span>' +
                    '</td>' +
                    '</tr>' +
                    '<tr style="width: 100%;font-weight:bold">' +
                    '<td style="width: 33%">' +
                    '<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>仓库：</b></span>' +
                    '<span style="font-size: 25px;">&nbsp;&nbsp;&nbsp;' +atrribute +"仓库"+ '</span>' +
                    '</td>' +
                    '</tr>' +
                    '<tr style="width: 100%;font-weight:bold">' +
                    '<td style="width: 33%;">' +
                    '<span>&nbsp;&nbsp;&nbsp;</span>' +
                    '<span>&nbsp;&nbsp;&nbsp;</span>' +
                    '</td>' +
                    '</tr>' +
                    '</table>' +
                    ' <table class="mainDiv" style="width: 100%;height: 10%;color: black;">' +
                    '<tr style="width: 100%;font-weight:bold">' +
                    '<td style="width: 33%;">' +
                    '<span><b>备料打印时间：</b></span>' +
                    '<span style="font-size: 21px">&nbsp;' +date + '</span>' +
                    '</td>' +
                    '<td style="width: 33%">' +
                    '<span>&nbsp;<b>最迟发车时间：</b></span>' +
                    '<span style="font-size: 21px">&nbsp;' +headData.latestTime + '</span>' +
                    '</td>' +
                    '<td style="width: 33%;padding-left: -10%; white-space: nowrap">' +
                    '<span ><b>预计到达时间：</b></span>' +
                    '<span style="font-size: 21px">&nbsp;' +headData.arrivedTime + '</span>' +
                    '</td>' +
                    '</tr>' +
                    '</table>' +
                    '<br>' +
                    '<table class="myTable"  style=" border: 3px solid black;  color: black;width: 100%;table-layout: fixed;font-size: 20px;text-align: center;" border="2px">'+
                    '<tr style="white-space: nowrap">'+
                    '<th style="text-align: center;width: 5%;">序号</th>'+
                    '<th style="text-align: center;width: 24%;">零件编码</th>'+
                    '<th style="text-align: center;width: 20%;">零件名称</th>'+
                    '<th style="text-align: center;width: 13%;">仓库货位</th>'+
                    '<th style="text-align: center;width: 12%;">计划备料<br>数量</th>'+
                    '<th style="text-align: center;width: 12%;">计划备货<br>箱数</th>'+
                    '<th style="text-align: center;width: 12%;">实际备料<br>数量</th>'+
                    '<th style="text-align: center;width: 7%;">备料人</th>'+
                    '<th style="text-align: center;width: 10%;">备注</th>'+
                    '</tr>';
            }
            nameplate = nameplate +
                '<tr>'+
                //第一列
                '<td  valign="middle" style="font-size: 25px; height:45px;width: 10%;word-break:break-all;">' + (i + 1)+
                '</td>'+
                //第二列
                '<td  valign="middle" style="font-size: 25px;height:45px;width: 40%;word-break:break-all;">' + a(lineData[i].itemCode, "")+
                '</td>'+
                //第三列
                '<td valign="middle" style="font-size: 25px;height:45px;width: 20%;word-break:break-all;">' + a((lineData[i].itemDesc).substring(0,8), "")+
                '</td>'+
                //第四列
                '<td valign="middle" style="font-size: 25px;height:45px;width: 10%;word-break:break-all;">' + a(lineData[i].attribute1, "")+
                '</td>'+
                //第五列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;">' + a(lineData[i].planQuantity, "")+
                '</td>'+
                //第六列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;">'+ a(lineData[i].executeQty, "")+
                '</td>'+
                //第七列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;">'+
                '</td>'+
                //第八列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;"> '+ a(lineData[i].distributeOrderAttribute, "")+
                '</td>'+
                //第九列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;">'+a(lineData[i].adviseLocator, "")+
                '</td>'+
                '</tr>'+
                '<div style="page-break-after:always"></div>';

            if(i==lineData.length-1)
            {
                if(lineData.length>30) {
                    if(lineData.length%30!=0) {
                        nameplate = nameplate + '</table>' +
                            '<table class="titleDiv" style="width: 100%;height: 5%;color: black;">' +
                            '<tr style="width: 100%;height: 5%;">' +
                            '</tr>' +
                            '<tr style="width: 100%;height: 5%;">' +
                            '<td style="width: 100%;text-align: center">' +
                            '<span style="font-size: 20px;padding-left:20px;text-align: center"><b>' + asd + '/' + b + '</b></span>' +
                            '</td>' +
                            '</tr>' +
                            '<tr>' +
                            '</tr>' +
                            '</table>' +
                            '</div>';
                    }
                }
                else
                {
                    nameplate =  nameplate+ '</table>'+
                        '<table class="titleDiv" style="width: 100%;height: 5%;color: black;">' +
                        '<tr style="width: 100%;height: 5%;">' +
                        '</tr>' +
                        '<tr style="width: 100%;height: 5%;">' +
                        '<td style="width: 100%;text-align: center">' +
                        '<span style="font-size: 20px;text-align: center"><b>'+asd+'/'+b+'</b></span>' +
                        '</td>' +
                        '</tr>' +
                        '<tr>' +
                        '</tr>' +
                        '</table>' +
                        '</div>';
                }
                $("#nameplateCode").html(nameplate); // 设置条码内容
                $('#' + codeImgId).barcode(
                    headData.distruibuteNumber,
                    "code128",
                    {
                        barWidth: 3,
                        barHeight: 120,
                        showHRI: false,
                        fontSize: 10
                    }
                );
                $("#nameplate").show();// 显示
                // 设置名称
                getImgStr("nameplateCode", i+'-'+printData.text + "-sq.png", isPrint.code, printData.printName);
            }
        }
    }
    else {
        var asd=1;
        var bd = 1;
        var headData = getHeadData("/hwm/item/distribute/line/queryItemDistribute", printData.text);
        var lineData = getLineData("/hwm/item/distribute/line/queryForPrintIn", printData.text);
        var codeImgId = "asnBarCode" + printData.index;
        var codeTd = "barCode" + printData.index;
        var lineInf = '';
        var atrribute = headData.attribute1;
        atrribute = atrribute.substring(0,2);
        bd = bye(lineData.length);
        var projectItemDesc = headData.projectItemDesc?headData.projectItemDesc.substring(0,10):"";
        nameplate = '<div id="nameplate" style="width: 120%;height: 200%; background-color: #FFF;font-size: 25px;">' +
            '<br>'+
            '<table class="titleDiv" style="width: 100%;color: black;">'+
            '<tr style="width: 100%;height: 5%;">'+
            '<td style="width: 100%;text-align: center">'+
            '<span style="font-size: 50px">投料单</span>'+
            '</td>'+
            '</tr>'+
            ' </table>'+
            ' <table class="mainDiv" style="width: 100%;height: 10%;color: black;">'+
            ' <tr  style="width: 100%;font-weight:bold">'+
            '<td style="width: 40%;text-align: center" rowspan="3"  id=" + codeTd + ">'+
            '<div  id=' + codeImgId + '>'+
            '</td>'+
            '<td style="width: 30% ">'+
            '<span style="white-space: nowrap">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>投料单号：' +headData.distruibuteNumber + '</b></span>'+
            '</td>'+
            '<td style="width: 30% ">' +
            '<span style="white-space: nowrap"><b>项目物料：'  + projectItemDesc + '</b></span>' +
            '</td>'+
            '</tr>'+
            '<tr style="width: 100%;font-weight:bold">'+
            '<td style="width: 33%;">'+
            '<span></span>'+
            '<span></span>'+
            '</td>'+
            '<td style="width: 33%;">'+
            '<span></span>'+
            '<span></span>'+
            '</td>'+
            '</tr>'+
            '<tr style="width: 100%;font-weight:bold">'+
            '<td style="width: 33%;">'+
            '<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>生产线：' +headData.description + '</b></span>'+
            '<span>&nbsp;&nbsp;&nbsp;</span>'+
            '</td>'+
            '<td style="width: 33%">'+
            '<span><b>仓库：</b></span>'+
            '<span>&nbsp' +atrribute +'仓库</span>'+
            '</td>'+
            '</tr>'+
            '<tr style="width: 100%;font-weight:bold">'+
            '<td style="width: 33%;">'+
            '<span>&nbsp;&nbsp;&nbsp;</span>'+
            '<span>&nbsp;&nbsp;&nbsp;</span>'+
            '</td>'+
            '</tr>'+
            '</table>'+
            ' <table class="mainDiv" style="width: 100%;height: 10%;color: black;">'+
            '<tr style="width: 100%;font-weight:bold">'+
            '<td style="width: 33%;">'+
            '<span><b>投料打印时间：</b></span>'+
            '<span style="font-size: 21px">&nbsp;&nbsp;' +date + '</span>'+
            '</td>'+
            '<td style="width: 33%">'+
            '<span>&nbsp;&nbsp;<b>最迟发车时间：</b></span>'+
            '<span style="font-size: 21px">&nbsp;&nbsp;' +headData.latestTime + '</span>'+
            '</td>'+
            '<td style="width: 33%;padding-left: -10%; white-space: nowrap">'+
            '<span >&nbsp;&nbsp;<b>预计到达时间：</b></span>'+
            '<span style="font-size: 21px">&nbsp;&nbsp;' +headData.arrivedTime + '</span>'+
            '</td>'+
            '</tr>'+
            '</table>'+
            '<br>'+
            '<table class="myTable"  style="border: 3px solid black;color: black;width: 100%;table-layout: fixed;font-size: 18.5px;text-align: center;" border="2px">'+
            '<tr style="white-space: nowrap">'+
            '<th style="text-align: center;width: 4%;">序号</th>'+
            '<th style="text-align: center;width: 19%;">零件编码</th>'+
            '<th style="text-align: center;width: 13%;">零件名称</th>'+
            '<th style="text-align: center;width: 12%;">缓冲区货位</th>'+
            '<th style="text-align: center;width: 12%;">线边地址</th>'+
            '<th style="text-align: center;width: 10%;">计划投料<br>数量</th>'+
            '<th style="text-align: center;width: 6%;">台套量</th>'+
            '<th style="text-align: center;width: 10%;">计划投料<br>箱数</th>'+
            '<th style="text-align: center;width: 10%;">实际投料<br>数量</th>'+
            '<th style="text-align: center;width: 6%;">投料人</th>'+
            '<th style="text-align: center;width: 8%;">备注</th>'+
            '</tr>';
        for(var i = 0 ; i<lineData.length;i++)
        {
            var acount = 0;
            lineData[i].planQuantity;
            if(lineData[i].codeId==null)
            {
                acount = "";
            }
            else
            {
                acount = Math.ceil(lineData[i].planQuantity/lineData[i].codeId);
            }
            if(lineData[i].adviseLocator==null)
            {
                lineData[i].adviseLocator = "";
            }
            if(i%30==0&&i!=0){

                nameplate =  nameplate+ '</table>'+
                    '<table class="titleDiv" style="width: 100%;height: 5%;color: black;">' +
                    '<tr style="width: 100%;height: 5%;">' +
                    '</tr>' +
                    '<tr style="width: 100%;height: 5%;">' +
                    '<td style="width: 100%;text-align: center">' +
                    '<span style="font-size: 20px;text-align: center"><b>'+asd+'/'+bd+'</b></span>' +
                    '</td>' +
                    '</tr>' +
                    '<tr>' +
                    '</tr>' +
                    '</table>' +
                    '</div>';
                asd++;
                $("#nameplateCode").html(nameplate); // 设置条码内容
                $('#' + codeImgId).barcode(
                    headData.distruibuteNumber ,
                    "code128",
                    {
                        barWidth: 3,
                        barHeight: 120,
                        showHRI: false,
                        fontSize: 10
                    }
                );
                $("#nameplate").show();// 显示
                // 设置名称
                getImgStr("nameplateCode", i+'-'+ printData.text + "-sq.png", isPrint.code, printData.printName);

                nameplate = '<div id="nameplate" style="width: 120%;height: 200%; background-color: #FFF;font-size: 25px;">' +
                    '<br>'+
                    '<table class="titleDiv" style="width: 100%;color: black;">'+
                    '<tr style="width: 100%;height: 5%;">'+
                    '<td style="width: 100%;text-align: center">'+
                    '<span style="font-size: 50px">投料单</span>'+
                    '</td>'+
                    '</tr>'+
                    ' </table>'+
                    ' <table class="mainDiv" style="width: 100%;height: 10%;color: black;">'+
                    ' <tr  style="width: 100%;font-weight:bold">'+
                    '<td style="width: 40%;text-align: center" rowspan="3"  id=" + codeTd + ">'+
                    '<div  id=' + codeImgId + '>'+
                    '</td>'+
                    '<td style="width: 30% ">'+
                    '<span style="white-space: nowrap">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>投料单号：' +headData.distruibuteNumber + '</b></span>'+
                    '</td>'+
                    '</tr>'+
                    '<tr style="width: 100%;font-weight:bold">'+
                    '<td style="width: 33%;">'+
                    '<span></span>'+
                    '<span></span>'+
                    '</td>'+
                    '<td style="width: 33%;">'+
                    '<span></span>'+
                    '<span></span>'+
                    '</td>'+
                    '</tr>'+
                    '<tr style="width: 100%;font-weight:bold">'+
                    '<td style="width: 33%;">'+
                    '<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>生产线：' +headData.description + '</b></span>'+
                    '<span>&nbsp;&nbsp;&nbsp;</span>'+
                    '</td>'+
                    '<td style="width: 33%">'+
                    '<span><b>仓库：</b></span>'+
                    '<span>&nbsp' +atrribute +'仓库</span>'+
                    '</td>'+
                    '</tr>'+
                    '<tr style="width: 100%;font-weight:bold">'+
                    '<td style="width: 33%;">'+
                    '<span>&nbsp;&nbsp;&nbsp;</span>'+
                    '<span>&nbsp;&nbsp;&nbsp;</span>'+
                    '</td>'+
                    '</tr>'+
                    '</table>'+
                    ' <table class="mainDiv" style="width: 100%;height: 10%;color: black;">'+
                    '<tr style="width: 100%;font-weight:bold">'+
                    '<td style="width: 33%;">'+
                    '<span><b>投料打印时间：</b></span>'+
                    '<span style="font-size: 21px">&nbsp;&nbsp;' +date + '</span>'+
                    '</td>'+
                    '<td style="width: 33%">'+
                    '<span>&nbsp;&nbsp;<b>最迟发车时间：</b></span>'+
                    '<span style="font-size: 21px">&nbsp;&nbsp;' +headData.latestTime + '</span>'+
                    '</td>'+
                    '<td style="width: 33%;padding-left: -10%; white-space: nowrap">'+
                    '<span >&nbsp;&nbsp;<b>预计到达时间：</b></span>'+
                    '<span style="font-size: 21px">&nbsp;&nbsp;' +headData.arrivedTime + '</span>'+
                    '</td>'+
                    '</tr>'+
                    '</table>'+
                    '<br>'+
                    '<table class="myTable"  style="border: 3px solid black;color: black;width: 100%;table-layout: fixed;font-size: 18.5px;text-align: center;" border="2px">'+
                    '<tr style="white-space: nowrap">'+
                    '<th style="text-align: center;width: 4%;">序号</th>'+
                    '<th style="text-align: center;width: 19%;">零件编码</th>'+
                    '<th style="text-align: center;width: 13%;">零件名称</th>'+
                    '<th style="text-align: center;width: 12%;">缓冲区货位</th>'+
                    '<th style="text-align: center;width: 12%;">线边地址</th>'+
                    '<th style="text-align: center;width: 10%;">计划投料<br>数量</th>'+
                    '<th style="text-align: center;width: 6%;">台套量</th>'+
                    '<th style="text-align: center;width: 10%;">计划投料<br>箱数</th>'+
                    '<th style="text-align: center;width: 10%;">实际投料<br>数量</th>'+
                    '<th style="text-align: center;width: 6%;">投料人</th>'+
                    '<th style="text-align: center;width: 8%;">备注</th>'+
                    '</tr>';

            }
            nameplate = nameplate +
                '<tr>'+
                //第一列
                '<td  valign="middle" style=" text-align: center; height:45px;width: 10%;word-break:break-all;">' + (i + 1)+
                '</td>'+

                //第二列
                '<td  valign="middle" style="height:45px;width: 40%;word-break:break-all;">' + a(lineData[i].itemCode, "")+
                '</td>'+

                //第三列
                '<td valign="middle" style="height:45px;width: 20%;word-break:break-all;">' + a((lineData[i].itemDesc).substring(0,8), "")+
                '</td>'+

                //第四列
                '<td valign="middle" style="height:45px;width: 10%;word-break:break-all;">'+ a(lineData[i].code, "")+
                '</td>'+

                //第五列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;">' + a(lineData[i].description, "")+
                '</td>'+

                //第六列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;">' + a(lineData[i].planQuantity, "")+
                '</td>'+
                //第七列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;">' + a(lineData[i].executeQty, "")+
                '</td>'+
                //第八列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;">'+acount+
                '</td>'+

                //第九列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;">'+
                '</td>'+

                //第十列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;">' + a(lineData[i].distributeOrderAttribute, "")+
                '</td>'+

                //第十一列
                '<td  valign="middle" style="height:45px;width: 10%;word-break:break-all;">'+lineData[i].adviseLocator+
                '</td>'+
                '</tr>'+
                '<div style="page-break-after:always"></div>';

            if(i==lineData.length-1)
            {
                if(lineData.length>30) {
                    if(lineData.length%30!=0) {
                        nameplate = nameplate + '</table>' +
                            '<table class="titleDiv" style="width: 100%;height: 5%;color: black;">' +
                            '<tr style="width: 100%;height: 5%;">' +
                            '</tr>' +
                            '<tr style="width: 100%;height: 5%;">' +
                            '<td style="width: 100%;text-align: center">' +
                            '<span style="font-size: 20px;padding-left:20px;text-align: center"><b>' + asd + '/' + bd + '</b></span>' +
                            '</td>' +
                            '</tr>' +
                            '<tr>' +
                            '</tr>' +
                            '</table>' +
                            '</div>';
                    }
                }
                else
                {
                    nameplate =  nameplate+ '</table>'+
                        '<table class="titleDiv" style="width: 100%;height: 5%;color: black;">' +
                        '<tr style="width: 100%;height: 5%;">' +
                        '</tr>' +
                        '<tr style="width: 100%;height: 5%;">' +
                        '<td style="width: 100%;text-align: center">' +
                        '<span style="font-size: 20px;text-align: center"><b>'+asd+'/'+bd+'</b></span>' +
                        '</td>' +
                        '</tr>' +
                        '<tr>' +
                        '</tr>' +
                        '</table>' +
                        '</div>';
                }
                $("#nameplateCode").html(nameplate); // 设置条码内容
                $('#' + codeImgId).barcode(
                    headData.distruibuteNumber,
                    "code128",
                    {
                        barWidth: 3,
                        barHeight: 120,
                        showHRI: false,
                        fontSize: 10
                    }
                );
                $("#nameplate").show();// 显示

                // 设置名称
                getImgStr("nameplateCode", i+'-'+ printData.text + "-sq.png", isPrint.code, printData.printName);
            }
        }
    }
}

/*
 获得单据头信息
 @param：headerId 单据头id
 @return：该单据头的数据
 Autor:ywj
 Date:2018/10/25
 */
function getHeadData(url, data) {
    var resultData = new Object();
    $.ajax({
        url: _basePath + url,
        type: "POST",
        dataType: "json",
        async: false,
        data: {
            distributeHeadId: data,
            page: 1,
            pageSize: 10000
        },
        success: function (result) {
            if (result.success) {
                if (result.rows.length > 0) {
                    resultData = result.rows[0];
                }
            }
        }
    });
    return resultData;
}

/*
    根据前台传入的送货单头id和行号范围，查询出指定范围的数据
     Autor:ywj
     Date:2018/10/25
    */
function getLineData(url, data) {
    var resultData = new Object();
    $.ajax({
        url: _basePath + url,
        type: "POST",
        dataType: "json",
        async: false,
        data: {
            distributeHeadId: data,
            page: 1,
            pageSize: 10000
        },
        success: function (result) {
            if (result.success) {
                resultData = result.rows;
            }
        }
    });
    return resultData;
}

function a(a,b) {
    if(a==null)
    {
        a = "";
        return a;
    }
    else
    {
        return a;
    }
}
function getImgStr(id, fileName, filePath, printName) {
    /**
     *
     * 描述: 将div根据id转图片流
     * @auther: ywj
     * @date: 2018/10/26 14:31
     * @param:id：DIV的id
     * @param:codeContent：条码名称
     * @param:filePath：服务器存储路径
     *
     */
    html2canvas($("#" + id), {
        onrendered: function (canvas) {
            canvas.id = "mycanvas";
            // 将div转成图片的字符流
            var img = convertCanvasToImage(canvas);
            // 设置需要传参参数 图片数据流 文件名称 文件存储路径
            var obj = {imgStr: img.src, fileName: fileName, filePath: filePath, printName: printName};
            // 发送请求至后台
            var printResult = postAjax("/print/internal/tracing/itemDistribute", obj);
            if (printResult.success) {

            } else {
                alert(printResult.message);
            }
        },
        background: 'white',// 背景白色
        width:1300,
        height: 1900

    });
}
function bye(a) {
    var b=0;
    if(a<=30)
    {
        b =1;
    }
    if(a>30&&a<=65)
    {
        b=2;
    }
    if(a>65)
    {
        b =  Math.ceil((a-30)/35)+1;
    }
    return b;
}
