package com.hand.utils.promptUtils.mapper;

import com.hand.utils.promptUtils.dto.PromptUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther:lkj
 * @Date:2018/6/19 11:03
 * @E-mail:kejin.liu@hand-china.com
 * @Description:多语言的导入导出
 */
public interface PromptUtilMapper {
    /**
     *
     * 功能描述: 查询表列名和注释
     *
     * @auther:lkj
     * @date:2018/6/19 上午11:26
     * @param:
     * @return:
     *
     * @param sql
     */
    List<PromptUtil> mySql(@Param("sql") String sql);


    void createTable(@Param("sql") String sql);

    void updateTable(String clmSql);
}
