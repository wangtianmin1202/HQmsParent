/**
 * Created by 刘克金 on 2017/12/12.
 * 查询系统参数  配置公用方法
 */
//调用销毁用户的方法

//判断是object 还是 array
function isArrayOne(arr){
    return Object.prototype.toString.call(arr) === "[object Array]";
}
//封装ajax
jQuery.myAjax = function(type,url,data){
    var resultData = new Object();
    var newData;
    var contentType;
    if(isArrayOne(data)){
        newData = JSON.stringify(data);
        contentType = "application/json";
    }else{
        newData = data;
        contentType = "application/x-www-form-urlencoded";
    }
    $.ajax({
        type:type,
        url:_basePath+url,
        data:newData,
        dataType:'json',
        contentType:contentType,
        async:false,
        success:function (result) {
            if(result.success){
                resultData = result;
                return resultData;
            }else{
                kendo.ui.showInfoDialog({
                    message: result.message
                });
            }
        }
    })
    return resultData;
}

function myPostAjax(type,url,data){
    var resultData = new Object();
    var newData;
    var contentType;
    if(isArrayOne(data)){
        newData = kendo.stringify(data);
        contentType = "application/json";
    }else{
        newData = data.toJSON();
        contentType = "application/x-www-form-urlencoded";
    }
    $.ajax({
        type:type,
        url:_basePath+url,
        data:newData,
        dataType:'json',
        contentType:contentType,
        async:false,
        success:function (result) {
            if(result.success){
                resultData = result;
                return resultData;
            }else{
                // return layer.msg('ERROR：'+result.message, {time:3000});
                kendo.ui.showInfoDialog({
                    message: result.message
                });
            }
        }
    })
    return resultData;
}

//判断是否为正整数
function isInteger(obj) {
    return Math.floor(obj) === obj
}
//js 除法运算，避免数据相除小数点后产生多位数和计算精度损失。
// num1被除数 | num2除数
function numDiv(num1, num2) {
    var baseNum1 = 0, baseNum2 = 0;
    var baseNum3, baseNum4;
    try {
        baseNum1 = num1.toString().split(".")[1].length;
    } catch (e) {
        baseNum1 = 0;
    }
    try {
        baseNum2 = num2.toString().split(".")[1].length;
    } catch (e) {
        baseNum2 = 0;
    }
    with (Math) {
        baseNum3 = Number(num1.toString().replace(".", ""));
        baseNum4 = Number(num2.toString().replace(".", ""));
        return (baseNum3 / baseNum4) * pow(10, baseNum2 - baseNum1);
    }
};
//获取字符窜的长度
var jmz = {};
jmz.GetLength = function(str) {
    ///<summary>获得字符串实际长度，中文2，英文1</summary>
    ///<param name="str">要获得长度的字符串</param>
    var realLength = 0, len = str.length, charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128) realLength += 1;
        else realLength += 2;
    }
    return realLength;
};
//打开页面是打印页面路径
var url = window.location.pathname.substring(1);
console.info(url);

//判断数据是否为空
function ifNotNull(data) {
    if(data != null && data !== "" && data != undefined){
        return true;
    }
    return false;
}

//时间格式转换 使用方法 new Date().Format("yyyy-MM-dd hh:mm:ss")
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
//JQueryCookie 使用方法
//1.添加一个"会话cookie"
// $.cookie('the_cookie', 'the_value');
//这里没有指明 cookie有效时间，所创建的cookie有效期默认到用户关闭浏览器为止，
// 所以被称为 “会话cookie（session cookie）”。

//2.创建一个cookie并设置有效时间为 7天
// $.cookie('the_cookie', 'the_value', { expires: 7 });
//这里指明了cookie有效时间，
// 所创建的cookie被称为“持久 cookie （persistent cookie）”。注意单位是：天；

//3.创建一个cookie并设置 cookie的有效路径
// $.cookie('the_cookie', 'the_value', { expires: 7, path: '/' });
//在默认情况下，只有设置 cookie的网页才能读取该 cookie。
// 如果想让一个页面读取另一个页面设置的cookie，必须设置cookie的路径。
// cookie的路径用于设置能够读取 cookie的顶级目录。将这个路径设置为网站的根目录，
// 可以让所有网页都能互相读取 cookie （一般不要这样设置，防止出现冲突）。

//4.读取cookie
//$.cookie('the_cookie');

//5.删除cookie
// $.cookie('the_cookie', null);   //通过传递null作为cookie的值即可

//6.可选参数
/*
$.cookie('the_cookie','the_value',{
 expires:7,//（Number|Date）有效期；设置一个整数时，单位是天；也可以设置一个日期对象作为Cookie的过期日期；
 path:'/',//（String）创建该Cookie的页面路径；
 domain:'jquery.com',//（String）创建该Cookie的页面域名；
 secure:true,//（Booblean）如果设为true，那么此Cookie的传输会要求一个安全协议，例如：HTTPS；
 raw: true,//默认值：false。 默认情况下，
 //读取和写入 cookie 的时候自动进行编码和解码（使用encodeURIComponent 编码，
 //decodeURIComponent 解码）。要关闭这个功能设置 raw: true 即可。
 })　
* */
jQuery.cookie = function(name, value, options) {
    if (typeof value != 'undefined') {
        options = options || {};
        if (value === null) {
            value = '';
            options = $.extend({}, options);
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString();
        }
        var path = options.path ? '; path=' + (options.path) : '';
        var domain = options.domain ? '; domain=' + (options.domain) : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};

/**
* @Description 遍历数组是否有相同的元素
* @author yuchao.wang
* @date 2019/8/26 11:35
*/
function contains(arr, val) {
    for(var i = 0; i < arr.length; i++){
        if(val === arr[i]){
            return true;
        }
    }
    return false;
}

/**
 * @Description 带有Echarts图表的导出Excel
 *              myChart:Echarts名,支持传递数组,可传null
 *              url:发送地址
 *              otherParams:其他追加参数,支持传递数组,需要传token 例:[{paramName:'otherParam', paramValue:'otherParam'}]
 * @author yuchao.wang
 * @date 2019/8/26 20:35
 */
function exportExcelWithEcharts(myCharts, url, otherParams) {
    //先将目标div追加在body里
    $("body").append('<div id="exportExcelWithEchartsDiv" style="display: none"></div>');

    //构造form表单
    var $form = $("<form>");
    $form.id='exportExcelWithEchartsForm';
    $form.attr({
        target: 'id_iframe',
        method: 'post',
        action: _basePath + url
    });

    //判断是否传入图表
    if(!!myCharts) {
        //构造图片参数
        var charts = [];
        if (!isArrayOne(myCharts)) {
            charts.push(myCharts);
        } else {
            charts = myCharts;
        }

        //遍历数组生成图片URL
        $.each(charts, function (i, v) {
            //将图表变成图片，生成格式为 undefineddata:image/png;base64, + [base64编码]（注意有逗号）
            var imgURL = v.getDataURL({
                pixelRatio: 1,
                type: "png",
                backgroundColor: "white"
            });
            var idx = i === 0 ? '' : i;
            $form.append($('<input>').attr({name: "img" + idx, value: imgURL, type: "hidden"}));
        });
    }

    //追加自定义参数
    var params = [];
    if(!isArrayOne(otherParams)) {
        params.push(otherParams);
    } else {
        params = otherParams;
    }
    $.each(params, function (i, v) {
        $form.append($('<input>').attr({name:v.paramName, value:v.paramValue, type:"hidden"}));
    });

    //填充并发送表单
    $("#exportExcelWithEchartsDiv").empty().append($form);
    $($form).submit();
    $("#exportExcelWithEchartsDiv").empty();
}

/**
 * 返回 值的 向上或者向下被n整除的数
 */
function getInteger(value,type,n) {
    if((value % n) == 0){
        return value;
    }
    //向上
    if(1 == type){
        if(value <0 ){
            return parseInt((value+n)/n-1) * n;
        }
        return parseInt((value+n)/n) * n;
    }
    //向下
    if(-1 == type){
        return getInteger(value-n,1,n);
    }
}

/**
 * 判断数据是否为空，不包括0
 */
function ifNull(val) {
    if(val == null || val === ""){
        return true
    }
    return false;
}