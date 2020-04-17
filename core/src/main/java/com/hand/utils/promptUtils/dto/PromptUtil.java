package com.hand.utils.promptUtils.dto;

/**
 * @Auther:lkj
 * @Date:2018/6/19 11:02
 * @E-mail:kejin.liu@hand-china.com
 * @Description:多语言的导入导出
 */
public class PromptUtil {

    private String code;//多语言的代码

    private String chinese;//中文描述

    private String english;//英文描述

    private String tableName;//表名

    private String columnName;//表列名

    private String columnDesc;//表列名描述

    private String databaseType;//数据库类型

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
    }
}
