package com.hand.spc.constants;

import com.hand.hap.core.BaseConstants;

import java.util.HashMap;
import java.util.Map;

public interface SpcConstants extends BaseConstants {
    /**
     * 新增状态
     */
    String ADD = "ADD";
    /**
     * 编辑 修改
     */
    String EDIT = "EDIT";
    /**
     * 副本保存
     */
    String COPY_SAVE = "COPY_SAVE";
    /**
     * 修改并新增关系
     */
    String ADD_EDIT = "ADD_EDIT";
    /**
     * 分类项扩展列数
     */
    String CLASSIFY_EXTEND_COUNT = "classifyExtendCount";
    /**
     * 拓展属性扩展列数
     */
    String ATTRIBUTE_EXTEND_COUNT = "attributeExtendCount";
    /**
     * 中文
     */
    String zh_CN = "zh_CN";
    /**
     * 图类型 主图
     */
    String M_TYPE = "M";
    /**
     * 图类型 次图
     */
    String S_TYPE = "S";
    /**
     * 计数型图形类型
     */
    String[] countChartType = {"nP","P","C","U"};
    /**
     * 计量型图形类型
     */
    String[] sampleChartType = {"XBAR-R","XBAR-S","Me-R","X-Rm"};
    /**
     * OOC状态
     */
    String PROCESSED = "PROCESSED";
    /**
     * OOC状态 未处理
     */
    String UNPROCESSED = "UNPROCESSED";

    Map<String,String> SPEC_CHAC = new HashMap<String, String>(){
     private static final long serialVersionUID = 1L;//可有可无
        {
            put("-", "RTYU");
        }
    };
    
    /**
     * 站点和租户的默认值
     */
    Long defaultTableValue = -1L;
}
