package com.hand.spc.pspc_sample_data.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_sample_data.dto.SampleData;
import com.hand.spc.pspc_sample_data.view.SampleDataQueryVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ISampleDataService extends IBaseService<SampleData>, ProxySelf<ISampleDataService> {

    /**
     * @param iRequest          基本参数
     * @param sampleDataQueryVO 限制条件
     * @param page              页数
     * @param pageSize          页数大小
     * @return : java.util.List<com.hand.spc.pspc_sample_data.dto.SampleDataQueryVO>
     * @Description: 基础数据查询
     * @author: ywj
     * @date 2019/8/14 17:32
     * @version 1.0
     */
    List<SampleDataQueryVO> queryBaseData(IRequest iRequest, SampleDataQueryVO sampleDataQueryVO, int page, int pageSize);

    /**
     * @param iRequest              基本参数
     * @param sampleDataQueryVOList 限制条件
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据保存
     * @author: ywj
     * @date 2019/8/15 16:36
     * @version 1.0
     */
    ResponseData saveBaseData(IRequest iRequest, List<SampleDataQueryVO> sampleDataQueryVOList);

    /**
     * @param iRequest          基本参数
     * @param request           文本传入参数
     * @param ceGroupId         控制要素组主键
     * @param ceParameterId     控件要素主键
     * @param attachmentGroupId 附件要素组主键
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据导入
     * @author: ywj
     * @date 2019/8/16 9:13
     * @version 1.0
     */
    ResponseData importExcel(IRequest iRequest, HttpServletRequest request, Long ceGroupId, Long ceParameterId, Long attachmentGroupId) throws NoSuchFieldException, IllegalAccessException;
}