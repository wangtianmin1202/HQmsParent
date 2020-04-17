package com.hand.utils.promptUtils.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.utils.promptUtils.dto.CreateReportFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther:lkj
 * @Date:2018/6/19 11:04
 * @E-mail:kejin.liu@hand-china.com
 * @Description:多语言的导入导出
 */
public interface IPromptUtilService {

    /**
     *
     * 功能描述: 查询表列名和注释并且导出
     *
     * @auther:lkj
     * @date:2018年09月17日11:37:19
     * @param:iRequest 用户请求
     * @param:dto 查询条件
     * @return:
     *
     */
    void selectTableColumn(String tableName, IRequest iRequest, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     *
     * 功能描述: 描述维护数据导入
     *
     * @auther:lkj
     * @date:2018/6/19 下午8:20
     * @param:
     * @return:
     *
     */
    ResponseData importPromptData(HttpServletRequest request, HttpServletResponse response, IRequest requestContext) throws Exception;
    /**
     *
     * 功能描述: excelUtil 建表
     *
     * @auther:lkj
     * @date:2018/7/11 下午7:22
     * @param:
     * @return:
     *
     */
    ResponseData importCreateTable(HttpServletRequest request, HttpServletResponse response, IRequest requestContext) throws Exception;
    /**
     *
     * 功能描述: 创建报表文件
     *
     * @auther:lkj
     * @date:2018/9/3 上午11:50
     * @param:
     * @return:
     *
     */
    ResponseData createReportFile(IRequest iRequest, CreateReportFile dto);
}
