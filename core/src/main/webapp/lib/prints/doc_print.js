/**
 * created by wei.zheng 2018-10-08
 */
/*
 获取url传参
 paramName:参数名称
 返回：参数值
 */
function GetQueryString(paramName) {
    var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)", "i");
    var r = decodeURI(window.location.search).substr(1).match(reg);
    if (r != null)
        return (r[2]);
    return null;
}
/*
 获得前台传入单据的headerId
 @return headerId的一维数组
 */
function getHeaderIdList() {
    var headerIdList = GetQueryString("headerIdList");
    return (headerIdList+"").split("-");
}

/*
 获得前台传入单据的功能代码
 @return
 */
function getFunc() {
    var funList = GetQueryString("func");
    return funList;
}

/*
 宽度方面：mm转换为px
 @param:mm 毫米数量
 @return：像素
 */
function tranMmToPxWidth(mm) {
    return (mm/25.4)*js_getDPI()[0];
}

/*
 高度方面：mm转换为px
 @param:mm 毫米数量
 @return：像素
 */
function tranMmToPxHeight(mm) {
    return (mm/25.4)*js_getDPI()[1];
}

/*
 查询并返回当前屏幕的分辨率
 @return:[0]宽度的分辨率
 @rerurn:[1]高度的分辨率
 */
function js_getDPI() {
    var arrDPI = new Array();
    if (window.screen.deviceXDPI != undefined) {
        arrDPI[0] = window.screen.deviceXDPI;
        arrDPI[1] = window.screen.deviceYDPI;
    }
    else {
        var tmpNode = document.createElement("DIV");
        tmpNode.style.cssText = "width:1in;height:1in;position:absolute;left:0px;top:0px;z-index:99;visibility:hidden";
        document.body.appendChild(tmpNode);
        arrDPI[0] = parseInt(tmpNode.offsetWidth);
        arrDPI[1] = parseInt(tmpNode.offsetHeight);
        tmpNode.parentNode.removeChild(tmpNode);
    }
    return arrDPI;
}

/*
 仿照oracle数据库的nvl(var,value)函数
 */
function nvl(variable,val) {
    if(isNull(variable)){
        return val;
    }else{
        return variable;
    }
}

/*
 *   判断variable是否有值
 *   如果有值，返回true
 *   否则，返回false
 * */
function isNull(variable) {
    if (variable != "" && variable != null && variable != undefined) {
        return false;
    }
    return true;
}