package com.hand.utils.promptUtils.dto;

/**
 * @Auther:lkj
 * @Date:2018/9/3 11:46
 * @E-mail:kejin.liu@hand-china.com
 * @Description: 创建报表文件
 */
public class CreateReportFile {

    private String projectUrl;

    private String packageUrl;

    private String createFileName;

    private String functionName;

    private String querySql;

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getPackageUrl() {
        return packageUrl;
    }

    public void setPackageUrl(String packageUrl) {
        this.packageUrl = packageUrl;
    }

    public String getCreateFileName() {
        return createFileName;
    }

    public void setCreateFileName(String createFileName) {
        this.createFileName = createFileName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }
}
