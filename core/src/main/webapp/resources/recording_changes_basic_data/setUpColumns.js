/**
 * 获取跟表名符合的数据
 * @param tableName 表名
 * @returns {*[]}
 */
function getColumns(tableName) {
    var columns = [
        {
            field: "tableName",
            title: '功能名称',
            width: 120,
            template: function (dataItem) {
                var v = dataItem.tableName ? dataItem.tableName : "";
                $.each(BASIC_DATA_HISTORY, function (i, n) {
                    if ((n.value || '').toLowerCase() == (v || '').toLowerCase()) {
                        v = n.meaning;
                        return v;
                    }
                })
                return v;
            }
        },
        {
            field: "userName",
            title: '操作人',
            width: 120,
        },
        {
            field: "lastUpdateDate",
            title: '操作时间',
            width: 150,
        },
        {
            field: "changeType",
            title: '行为',
            width: 120,
            template: function (dataItem) {
                var v = dataItem.changeType ? dataItem.changeType : "";
                if(v == "添加"){
                    return '<span style="color: green">'+v+'</span>';
                }else if(v == "修改"){
                    return '<span style="color: #ffb01f">'+v+'</span>';
                }else{
                    return '<span style="color: red">'+v+'</span>';
                }
                return v;
            }
        },
    ];
    var newCol = new Array();
    if (tableName == "HQM_SAMPLE_MANAGE") {
        newCol = HQM_SAMPLE_MANAGE;
    } else if (tableName == "hcm_production_line") {
        newCol = HCM_PRODUCTION_LINE;
    }

    for (var i = 0; i < newCol.length; i++) {
        var columnsObj = getColumn(newCol[i].value, newCol[i].meaning, parseInt(newCol[i].tag), newCol[i].description);
        columns.push(columnsObj);
    }
    return columns;
}

/**
 * 获取需要展示的字段
 * @param field 字段
 * @param title 标题
 * @param width 列宽
 * @param isCode 是否为快速编码
 * @param codeName 快速编码值
 * @returns {*}
 */
function getColumn(field, title, width, isCode, codeName) {
    if (isCode == "N" || isCode == null || isCode == undefined || isCode == "") {
        return {
            field: field,
            title: title,
            width: width,
        };
    } else {
        return {
            field: field,
            title: title,
            width: width,
            template: function (dataItem) {
                var text = getCode(field, dataItem);

                return text;
            }
        };
    }
}

/**
 * 获取快速编码的
 * @param field 字段名
 * @param values 值
 * @returns {*} 返回显示值
 */
function getCode(field, values) {

    if (field == "sampleType") {
        var v = values.sampleType;
        if (v == null || v == "" || v == undefined) {
            return "";
        }
        for(var i=0;i<sampleTypeData.length;i++){
            if(v == sampleTypeData[i].value){
                return sampleTypeData[i].meaning;
            }
        }

    }
}
