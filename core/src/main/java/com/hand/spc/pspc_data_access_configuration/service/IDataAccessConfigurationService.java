package com.hand.spc.pspc_data_access_configuration.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_data_access_configuration.dto.DataAccessConfiguration;

import java.util.List;

public interface IDataAccessConfigurationService extends IBaseService<DataAccessConfiguration>, ProxySelf<IDataAccessConfigurationService> {

    /**
    * @Description 保存
    * @author hch
    * @date 2019/8/7 15:25
    * @Param [requestCtx, dto]
    * @return com.hand.hap.system.dto.ResponseData
    * @version 1.0
    */
    ResponseData updateAndSubmit(IRequest requestCtx, DataAccessConfiguration dto);

    /**
    * @Description 删除
    * @author hch
    * @date 2019/8/7 18:05
    * @Param [dto]
    * @return com.hand.hap.system.dto.ResponseData
    * @version 1.0
    */
    ResponseData remove(DataAccessConfiguration dto);


    /**
    * @Description 查询
    * @author hch
    * @date 2019/8/8 11:25
    * @Param [requestContext, dto, page, pageSize]
    * @return java.util.List<demo.pspc_data_access_configuration.dto.DataAccessConfiguration>
    * @version 1.0
    */
    List<DataAccessConfiguration> selectData(IRequest requestContext, DataAccessConfiguration dto, int page, int pageSize);

    /**
    * @Description 校验数据（过滤下限不能超过过滤上限，唯一性校验）
    * @author hch
    * @date 2019/8/8 17:11
    * @Param [requestCtx, dto]
    * @return com.hand.hap.system.dto.ResponseData
    * @version 1.0
    */
    ResponseData validate(IRequest requestCtx, List<DataAccessConfiguration> dto);
}