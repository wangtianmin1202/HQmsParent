package com.hand.sys.sys_individuation_query.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.sys.sys_individuation_query.dto.IndividuationQuery;
import com.hand.sys.sys_individuation_query.dto.IndividuationVO;

import java.util.List;

public interface IIndividuationQueryService extends IBaseService<IndividuationQuery>, ProxySelf<IIndividuationQueryService>{

    /**
     * 根据功能编码查询列
     * @param code 功能编码
     * @return List集合
     */
    public List<IndividuationQuery> selectColumnByFunction(String code);

    /**
     * 保存用户查询模板
     * @param request
     * @param vo
     */
    public void saveTemplate(IRequest request,IndividuationVO vo);

    /**
     * 查询数据
     * @param request
     * @param vo
     */
    public String queryData(IRequest request,IndividuationVO vo);

}