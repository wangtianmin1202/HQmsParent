/*
 * Create By:    lkj
 * Create Date:  2018年08月27日10:10:37
 * Desc:         打印工具
 *
 * */

function MyPrint(printData) {
    /**
     *
     * 描述: 该方法判断进入哪一个打印方法
     * @auther: lkj
     * @date: 2018/8/28 上午9:21
     *
     */
    // 根据类型判断进入那个方法
    // printData.type = "TEST2";
    // printData.text = "000002RSEOT";
    // printData.code = "21442650";
    // printData.printName = "Microsoft Print to PDF";
    if (printData.type == "inside") {//打印一维码内部追溯条码
        InternalTracingBarCode(printData);
    } else if (printData.type == "nationalStandard") {//国标客户追溯条码
        NationalStandardCode(printData);
    } else if (printData.type == "namePlatePrint") {//铭牌打印
        NamePlatePrint(printData);
    } else if (printData.type == "gmCode") {//通用客户条码
        gmStandardCode(printData);
    }
}

function K288ABatteryPackCustomerTraceabilityBarCode(printData) {
    /**
     *
     * 描述:
     * @auther: lkj
     * @date: 2018/8/28 上午9:33
     * @param:printData 需要打印的数据
     */
        // 先进行判断该条码是否已经打印过，如果打印过则直接打印 如果没有打印则获取服务器照片存储路径
    var fileName = printData.text + "-2.png";
    var isPrint = postAjax("/is/print", {fileName: fileName, printName: printData.printName});
    if (isPrint.success) {
        return;
    } else {
        if (isPrint.message != null && isPrint.message != "" && isPrint.message != undefined) {
            alert(isPrint.message);
            return;
        }
    }

    var codes = printData.code1.split(',');
    var gmVpps = codes[0];
    var gmItemCode1 = codes[1].substring(0, 4);
    var gmItemCode2 = codes[1].substring(4, 8);
    var gmSupplierCode = codes[2];
    var gm4thCode = codes[3];
    var barCode = '<div id="TEST2" style="width: 1500px;height: 500px;background-color: #ffffff">';
    barCode += '<div id="towCode" style="float: left;margin-left: 11%;margin-top: 3%">' +
        '<canvas id="code1" style="height: 200px"></canvas></div>';
    barCode += '<div id="content" style="float: left;margin:1% 10px 20px -200px;">';
    barCode += '<span><font color="black" size="6px">' + gmItemCode1 + '</font><font color="black" size="8px">' + gmItemCode2 + '</font></span><br/>';
    barCode += '<span><font color="black" size="6px">' + gmVpps + '</font></span><br/>';
    barCode += '<span><font color="black" size="6px">' + gmSupplierCode + '</font></span><br/>';
    barCode += '<span><font color="black" size="6px">' + gm4thCode + '</font></span>';
    barCode += '</div>';
    barCode += '<div id="towCode2" style="float: left;margin-top: 3%;">' +
        '<canvas id="code2" style="height: 280px"></canvas></div>';
    barCode += '</div>';
    var width = 200;// 二维码宽高
    var height = 200;
    $("#barCode2").html(barCode); // 设置条码内容
    $("#TEST2").show(); // 显示
    // 宽高必须设置，否则无法上传
    // var o = document.getElementById("TEST2");
    // var h = o.offsetHeight; //高度
    // var w = o.offsetWidth; //宽度
    // console.info(h+","+w);
    //$("#TEST2").attr("style", "width:1500px;height:245px");
    $("#code1").barcode(
        '[)>' + String.fromCharCode('30') + '06' + String.fromCharCode('29') + 'Y' + gmVpps + String.fromCharCode('29') + 'P'
        + gmItemCode1 + gmItemCode2 + String.fromCharCode('29') + '12V' +
        gmSupplierCode + String.fromCharCode('29') + 'T' +
        gm4thCode + String.fromCharCode('30') + String.fromCharCode('4'),
        "datamatrix", {
            showHRI: false,
            output: 'canvas'
        });
    $("#code2").barcode(printData.code2, "datamatrix", {
        showHRI: false,
        output: 'canvas'
    });
    // $('#towCode').qrcode({
    //     render: "canvas",
    //     text: printData.code1,
    //     width: width,
    //     height: height,
    // });
    // $('#towCode2').qrcode({
    //     render: "canvas",
    //     text: printData.code2,
    //     width: width,
    //     height: height,
    // });
    // 设置名称 一维码或二维码内容 加类型 一维码为-1  二维码为-2
    getImgStr("TEST2", fileName, isPrint.code, printData.printName);
}

/**
 * 国标码信息打印
 * @param printData
 * @constructor
 */
function NationalStandardCode(printData) {
    var responseData = postAjax("/printNationalCode/nationalcode", {
        batteryCode: printData.text,
        printName: printData.printName
    });
    if (!responseData.success) {
        alert(responseData.message);
    } else {
        console.log(printData.text + ",国标码打印成功");
    }
    // // 先进行判断该条码是否已经打印过，如果打印过则直接打印 如果没有打印则获取服务器照片存储路径
    // var fileName = printData.text + "-cs.png";
    // var isPrint = postAjax("/is/print", {fileName: fileName, printName: printData.printName});
    // if (isPrint.success) {
    //     return;
    // } else {
    //     if (isPrint.message != null && isPrint.message != "" && isPrint.message != undefined) {
    //         alert(isPrint.message);
    //         return;
    //     }
    // }
    // var fontStyle = "clear:both; background-color: #FFFFFF; color: #000000; font-size: 24px; margin-top: -7%;margin-left:25%;font-family: '微软雅黑', '宋体', Arial, sans-serif";
    // var barCode = '<div id="clientStandard" style="width: 1500px;height: 500px;background-color: #ffffff">';
    // barCode += '<div id="towCode1">' +
    //     '<canvas id="code1" style="height: 280px;margin:0% 0% 0% 30%"></canvas></div>';
    // barCode += '<div id="text"></div>'
    // barCode += '</div>';
    // var width = 200;// 二维码宽高
    // var height = 200;
    // $("#clientStandardCode").html(barCode); // 设置条码内容
    // $("#clientStandard").show(); // 显示
    // $("#code1").barcode(printData.code1, "datamatrix", {
    //     showHRI: false,
    //     output: 'canvas'
    // });
    // $("#text").attr("style", fontStyle);
    // $("#text").html(printData.code1);
    // // 设置名称 一维码或二维码内容 加类型 一维码为-1  二维码为-2
    // getImgStr("clientStandard", fileName, isPrint.code, printData.printName);
}

/**
 * 客户码打印
 * @param printData
 * @constructor
 */
function gmStandardCode(printData) {
    var codes = printData.code2.split(',');
    var gmVpps = codes[0];
    var gmItemCode = codes[1];
    var gmItemCode1 = codes[1].substring(0, 4);
    var gmItemCode2 = codes[1].substring(4, 8);
    var gmSupplierCode = codes[2];
    var gm4thCode = codes[3];
    var gmDataMatrixCode = '[)>' + String.fromCharCode('30') + '06' + String.fromCharCode('29') + 'Y' + gmVpps +
        String.fromCharCode('29') + 'P' + gmItemCode1 + gmItemCode2 + String.fromCharCode('29') + '12V' +
        gmSupplierCode + String.fromCharCode('29') + 'T' + gm4thCode + String.fromCharCode('30') +
        String.fromCharCode('4');
    var responseData = postAjax("/printGmCode/gmcode", {
        gmPartCode: gmItemCode,
        gmVpps: gmVpps,
        gmSupplierCode: gmSupplierCode,
        gmSerialNumber: gm4thCode,
        gmDataMatrixCode: gmDataMatrixCode,
        printName: printData.printName
    });
    if (!responseData.success) {
        alert(responseData.message);
    } else {
        console.log(printData.text + ",通用码打印成功");
    }
    // 先进行判断该条码是否已经打印过，如果打印过则直接打印 如果没有打印则获取服务器照片存储路径
    // var fileName = printData.text + "-gs.png";
    // var isPrint = postAjax("/is/print", {fileName: fileName, printName: printData.printName});
    // if (isPrint.success) {
    //     return;
    // } else {
    //     if (isPrint.message != null && isPrint.message != "" && isPrint.message != undefined) {
    //         alert(isPrint.message);
    //         return;
    //     }
    // }

    // var codes = printData.code2.split(',');
    // var gmVpps = codes[0];
    // var gmItemCode1 = codes[1].substring(0, 4);
    // var gmItemCode2 = codes[1].substring(4, 8);
    // var gmSupplierCode = codes[2];
    // var gm4thCode = codes[3];
    // var barCode = '<div id="nationalStandard" style="width: 1500px;height: 500px;background-color: #ffffff">';
    // barCode += '<div id="towCode2">' +
    //     '<canvas id="code2" style="height: 200px;margin:5% 0% 0% 20%"></canvas></div>';
    // barCode += '<div id="content" style="margin:-15% 10px 20px 35%;float:left">';
    // barCode += '<span><font color="black" size="6px">' + gmItemCode1 + '</font><font color="black" size="8px">' + gmItemCode2 + '</font></span><br/>';
    // barCode += '<span><font color="black" size="6px">' + gmVpps + '</font></span><br/>';
    // barCode += '<span><font color="black" size="6px">' + gmSupplierCode + '</font></span><br/>';
    // barCode += '<span><font color="black" size="6px">' + gm4thCode + '</font></span>';
    // barCode += '</div>';
    // barCode += '</div>';
    // var width = 200;// 二维码宽高
    // var height = 200;
    // $("#nationalStandardCode").html(barCode); // 设置条码内容
    // $("#nationalStandard").show(); // 显示
    // $("#code2").barcode(
    //     '[)>' + String.fromCharCode('30') + '06' + String.fromCharCode('29') + 'Y' + gmVpps + String.fromCharCode('29') + 'P'
    //     + gmItemCode1 + gmItemCode2 + String.fromCharCode('29') + '12V' +
    //     gmSupplierCode + String.fromCharCode('29') + 'T' +
    //     gm4thCode + String.fromCharCode('30') + String.fromCharCode('4'),
    //     "datamatrix", {
    //         showHRI: false,
    //         output: 'canvas'
    //     });
    // // 设置名称 一维码或二维码内容 加类型 一维码为-1  二维码为-2
    // getImgStr("nationalStandard", fileName, isPrint.code, printData.printName);
}

function InternalTracingBarCode(printData) {
    // var responseData = postAjax("/printInternalCode/internalcode", {
    //     batteryCode: printData.text,
    //     batteryType: printData.code,
    //     printName: printData.printName
    // });
    // if (!responseData.success) {
    //     alert(responseData.message);
    // } else {
    //     console.log(printData.text + ",内部码打印成功");
    // }
        // 先进行判断该条码是否已经打印过，如果打印过则直接打印 如果没有打印则获取服务器照片存储路径
    var fileName = printData.text + "-1.png";
    var isPrint = postAjax("/is/print", {fileName: fileName, printName: printData.printName});
    if (isPrint.success) {
        return;
    } else {
        if (isPrint.message != null && isPrint.message != "" && isPrint.message != undefined) {
            alert(isPrint.message);
            return;
        }
    }
    // 设置条码布局 PS可以先在工具上画好再复制过来
    // 改字体样式和条码样式一样，可以自定义更改
    var fontStyle = "clear:both; background-color: #FFFFFF; color: #000000; font-size: 24px; margin-top: 1px;margin-left:32%;font-family: '微软雅黑', '宋体', Arial, sans-serif";
    var barCode = '<div id="TEST" style="width: 1248px;height: 416px">';
    barCode += '<br><br><div id="bcTarget" style="margin-left: 10%;margin-top: 0mm"></div>';
    barCode += '<div id="code"></div>';
    barCode += '</div>';
    $("#barCode").html(barCode);// 设置条码内容
    $("#TEST").show();// 显示
    $("#bcTarget").barcode(printData.text, "code128", {barWidth: 3, barHeight: 120, fontSize: 24});
    fontStyle += "width:" + $("#bcTarget").width() + "px;";
    //$("#TEST").attr("style", "width:" + $("#bcTarget").width() + "px;");
    $("#code").attr("style", fontStyle);// K288A电池包客户追溯条码
    $("#code").html(printData.code);// 设置显示条码
    // 设置名称 一维码或二维码内容 加类型 一维码为-1  二维码为-2
    getImgStr("TEST", fileName, isPrint.code, printData.printName);
}

/**
 * @author: Benjamin
 * @date: 2018/10/25 10:54
 * @param printData
 * @constructor
 */
function NamePlatePrint(printData) {
    var responseData = postAjax("/print/nameplate", {batteryCode: printData.text, printName: printData.printName});
    if (!responseData.success) {
        alert(responseData.message);
    } else {
        console.log(printData.text + ",铭牌打印成功");
    }
    // // 先进行判断该条码是否已经打印过，如果打印过则直接打印 如果没有打印则获取服务器照片存储路径
    // var fileName = printData.text + "-np.png";
    // var isPrint = postAjax("/is/print", {fileName: fileName, printName: printData.printName});
    // if (isPrint.success) {
    //     return;
    // } else {
    //     if (isPrint.message != null && isPrint.message != "" && isPrint.message != undefined) {
    //         alert(isPrint.message);
    //         return;
    //     }
    // }
    // var date = new Date();
    // var obj = {batteryCode: printData.text};
    // var result = postAjax('/hme/sc/nameplate/info/query/by/battery/code', obj);
    // var nameplate = '<div id="nameplate" style="margin-left:3%;margin-top:10%;width:1000px;background-color:#FFFFFF;height: 400px;font-size: 10px;">' +
    //     '<div><<br><br><span style="margin-left: 25%;color:#000000;font-size:24px">产品铭牌</span></div>' +
    //     '<table style="margin:0% 0% 0% 5%;font-size: 20px;background-color: #FFFFFF" cellpadding="5px" cellspacing="5px">' +
    //     '<tbody style="background-color: #ffffff">' +
    //     '<tr style="background-color: #ffffff;"><font color="black">' +
    //     '<td style="background-color: #ffffff"><font color="black">&nbsp产品名称:</td>' +
    //     '<td style="background-color: #ffffff"><font color="black"><u>' + result.rows[0].productionName + '</u></td>' +
    //     '<td style="background-color: #ffffff"><font color="black">&nbsp产品型号(公告):</td>' +
    //     '<td style="background-color: #ffffff"><font color="black"><u>' + result.rows[0].productionType + '</u></td>' +
    //     '</tr>' +
    //     '<tr style="background-color: #ffffff"><font color="black">' +
    //     '<td style="background-color: #ffffff"><font color="black">&nbsp化学类型:</td>' +
    //     '<td style="background-color: #ffffff"><font color="black"><u>' + result.rows[0].chemicalType + '</u></td>' +
    //     '<td style="background-color: #ffffff"><font color="black">&nbsp执行标准:</td>' +
    //     '<td style="background-color: #ffffff"><font color="black"><u>' + result.rows[0].executiveStandard + '</u></td>' +
    //     '</tr>' +
    //     '<tr style="background-color: #ffffff"><font color="black">' +
    //     '<td style="background-color: #ffffff"><font color="black">&nbsp额定能量:</td>' +
    //     '<td style="background-color: #ffffff"><font color="black"><u>' + result.rows[0].ratedEnergy + '</u></td>' +
    //     '<td style="background-color: #ffffff"><font color="black">&nbsp生产厂商:</td>' +
    //     '<td style="background-color: #ffffff"><font color="black"><u>' + result.rows[0].producer + '</u></td>' +
    //     '</tr>' +
    //     '<tr style="background-color: #ffffff"><font color="black">' +
    //     '<td style="background-color: #ffffff"><font color="black">&nbsp额定容量:</td>' +
    //     '<td style="background-color: #ffffff"><font color="black"><u>' + result.rows[0].ratedCapacity + '</u></td>' +
    //     '<td style="background-color: #ffffff"><font color="black">&nbsp厂址:</td>' +
    //     '<td style="background-color: #ffffff"><font color="black"><u>' + result.rows[0].address + '</u></td>' +
    //     '</tr>' +
    //     '<tr style="background-color: #ffffff"><font color="black">' +
    //     '<td style="background-color: #ffffff"><font color="black">&nbsp标称电压:</td>' +
    //     '<td style="background-color: #ffffff"><font color="black"><u>' + result.rows[0].nominalVoltage + '</u></td>' +
    //     '<td style="background-color: #ffffff"><font color="black">&nbsp生产日期:</td>' +
    //     '<td style="background-color: #ffffff"><font color="black"><u>' + result.rows[0].productDate + '</u></td>' +
    //     '</tr>' +
    //     '</tbody>' +
    //     '</table>' +
    //     '</div>';
    // $("#nameplateCode").html(nameplate); // 设置条码内容
    // $("#nameplate").show();// 显示
    // // 设置名称 铭牌为TEST3
    // getImgStr("nameplate", fileName, isPrint.code, printData.printName);
}

function getImgStr(id, fileName, filePath, printName) {
    /**
     *
     * 描述: 将div根据id转图片流
     * @auther: lkj
     * @date: 2018/8/28 上午9:20
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
            var printResult = postAjax("/print/internal/tracing/bar/code", obj);
            if (printResult.success) {

            } else {
                alert(printResult.message);
            }
            $("#" + id).hide();// 隐藏
        },
        background: 'white'// 背景白色
    });
}

function postAjax(url, data) {
    /**
     *
     * 描述: 封装ajax
     * @auther: lkj
     * @date: 2018/8/28 上午9:21
     *
     */
    var resultData = new Object();
    $.ajax({
        type: "POST",
        url: _basePath + url,
        data: data,
        dataType: 'json',
        contentType: 'application/x-www-form-urlencoded',
        async: false,
        success: function (result) {
            resultData = result;
        }
    })
    return resultData;
}


